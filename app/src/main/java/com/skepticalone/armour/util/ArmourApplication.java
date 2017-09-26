package com.skepticalone.armour.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.skepticalone.armour.R;

import org.threeten.bp.ZoneId;

public final class ArmourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String timeZoneIdKey = getString(R.string.key_time_zone_id);
        if (!sharedPreferences.contains(timeZoneIdKey)) {
            sharedPreferences.edit().putString(timeZoneIdKey, ZoneId.systemDefault().getId()).apply();
        }
    }
}
