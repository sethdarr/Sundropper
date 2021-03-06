package com.sundropelectric.sundropper.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.wallet.Wallet;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;

public class GooglePlayServicesActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GooglePlayServicesActivity";

    private static final String KEY_IN_RESOLUTION = "is_in_resolution";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }
    }

    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Google Play Services are not available on this device.  Aborting...", Toast.LENGTH_LONG).show();
            finish();
        } else if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    //.addApi(Cast.API)
                    .addApi(Drive.API)
                    .addApi(Plus.API)
                            //.addApi(Wallet.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                            // Optionally, add additional APIs and scopes if required.
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

        }
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                if (resultCode == RESULT_OK && !mIsInResolution) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    String msg = "Selected file's ID: " + driveId;
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    Log.i(TAG, msg);
                    finish();
                    break;
                }

                retryConnecting();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    ResultCallback<DriveApi.ContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveApi.ContentsResult>() {
                @Override
                public void onResult(DriveApi.ContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // display an error saying file can't be opened
                        return;
                    }
                    // Contents object contains pointers
                    // to the actual byte stream
                    Contents contents = result.getContents();
                }
            };

    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        // TODO: Start making API requests.

        //       file.openContents(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(contentsOpenedCallback);
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()

                .setMimeType(new String[]{"audio/mpeg", "application/vnd.google-apps.document"})
                .build(mGoogleApiClient);

        try {
            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_RESOLUTION, null, 0, 0, 0);
        } catch (SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }
    }

    /**
     * Called when {@code mGoogleApiClient} connection is suspended.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }
            ).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }
}
