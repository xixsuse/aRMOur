package com.skepticalone.armour.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public final class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment(), SETTINGS_FRAGMENT)
                    .commit();
        }
    }

}