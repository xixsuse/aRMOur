package com.skepticalone.mecachecker.components;

import android.preference.PreferenceActivity;

import com.skepticalone.mecachecker.R;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return fragmentName.equals(SettingsFragment.class.getName()) || super.isValidFragment(fragmentName);
    }
}