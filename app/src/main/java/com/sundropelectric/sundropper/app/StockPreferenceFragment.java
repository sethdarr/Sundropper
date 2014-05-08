package com.sundropelectric.sundropper.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by seth.darr on 5/7/2014.
 */
public class StockPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int res = getActivity()
                .getResources()
                .getIdentifier(getArguments().getString("resource"),
                        "xml", getActivity().getPackageName());
        addPreferencesFromResource(res);
    }
}
