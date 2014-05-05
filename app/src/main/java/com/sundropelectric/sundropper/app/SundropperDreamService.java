package com.sundropelectric.sundropper.app;

import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.dreams.DreamService;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This class is a sample implementation of a DreamService. When activated, a
 * TextView will repeatedly, move from the left to the right of screen, at a
 * random y-value.
 * <p />
 * Daydreams are only available on devices running API v17+.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SundropperDreamService extends DreamService {

    private static final TimeInterpolator sInterpolator = new LinearInterpolator();

    private final AnimatorListener mAnimListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            // Start animation again
            startImageViewScrollAnimation();
        }

    };

    private final Random mRandom = new Random();
    private final Point mPointSize = new Point();

    private ImageView mDreamImageView;
    private ViewPropertyAnimator mAnimator;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Exit dream upon user touch?
        setInteractive(false);

        // Hide system UI?
        setFullscreen(true);

        // Keep screen at full brightness?
        setScreenBright(false);

        // Set the content view, just like you would with an Activity.
        setContentView(R.layout.sundropper_dream);

        mDreamImageView = (ImageView) findViewById(R.id.dream_image);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        // TODO: Begin animations or other behaviors here.

        startImageViewScrollAnimation();
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();

        // TODO: Stop anything that was started in onDreamingStarted()

        mAnimator.cancel();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // TODO: Dismantle resources
        // (for example, detach from handlers and listeners).
    }

    private void startImageViewScrollAnimation() {
        // Refresh Size of Window
        getWindowManager().getDefaultDisplay().getSize(mPointSize);

        final int windowWidth = mPointSize.x;
        final int windowHeight = mPointSize.y;

        // Move TextView so it's moved all the way to the left
        mDreamImageView.setTranslationX(-mDreamImageView.getWidth());

        // Move TextView to random y value
        final int yRange = windowHeight - mDreamImageView.getHeight();
        mDreamImageView.setTranslationY(mRandom.nextInt(yRange));

        // Create an Animator and keep a reference to it
        mAnimator = mDreamImageView.animate().translationX(windowWidth)
                .setDuration(6000)
                .setStartDelay(500)
                .setListener(mAnimListener)
                .setInterpolator(sInterpolator);

        // Start the animation
        mAnimator.start();
    }
}
