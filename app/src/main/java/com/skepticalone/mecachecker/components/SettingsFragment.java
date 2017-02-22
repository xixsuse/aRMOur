package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.skepticalone.mecachecker.R;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shift_preferences);
    }
}
