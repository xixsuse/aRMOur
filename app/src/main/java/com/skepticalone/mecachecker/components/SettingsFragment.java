package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.skepticalone.mecachecker.R;

public class SettingsFragment extends PreferenceFragment {
//
//    public static final String TAG = "SettingsFragment";
//    private String
//            mKeyStartNormalDay,
//            mKeyEndNormalDay,
//            mKeyStartLongDay,
//            mKeyEndLongDay,
//            mKeyStartNightShift,
//            mKeyEndNightShift;
//
//    private Preference mNormalDayPreference, mLongDayPreference, mNightShiftPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shift_preferences);
//        mNormalDayPreference = findPreference(getString(R.string.key_overview_normal_day));
//        mLongDayPreference = findPreference(getString(R.string.key_overview_long_day));
//        mNightShiftPreference = findPreference(getString(R.string.key_overview_night_shift));
//        mKeyStartNormalDay = getString(R.string.key_start_normal_day);
//        mKeyEndNormalDay = getString(R.string.key_end_normal_day);
//        mKeyStartLongDay = getString(R.string.key_start_long_day);
//        mKeyEndLongDay = getString(R.string.key_end_long_day);
//        mKeyStartNightShift = getString(R.string.key_start_night_shift);
//        mKeyEndNightShift = getString(R.string.key_end_night_shift);
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume() called");
//        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause() called");
//        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Log.d(TAG, "onSharedPreferenceChanged() called with: sharedPreferences = [" + sharedPreferences + "], key = [" + key + "]");
//        Preference preference;
//        int startTotalMinutes, endTotalMinutes;
//        if (key.equals(mKeyStartNormalDay) || key.equals(mKeyEndNormalDay)) {
//            preference = mNormalDayPreference;
//            startTotalMinutes = sharedPreferences.getInt(mKeyStartNormalDay, getResources().getInteger(R.integer.default_start_normal_day));
//            endTotalMinutes = sharedPreferences.getInt(mKeyEndNormalDay, getResources().getInteger(R.integer.default_end_normal_day));
//        } else if (key.equals(mKeyStartLongDay) || key.equals(mKeyEndLongDay)) {
//            preference = mLongDayPreference;
//            startTotalMinutes = sharedPreferences.getInt(mKeyStartLongDay, getResources().getInteger(R.integer.default_start_long_day));
//            endTotalMinutes = sharedPreferences.getInt(mKeyEndLongDay, getResources().getInteger(R.integer.default_end_long_day));
//        } else if (key.equals(mKeyStartNightShift) || key.equals(mKeyEndNightShift)) {
//            preference = mNightShiftPreference;
//            startTotalMinutes = sharedPreferences.getInt(mKeyStartNightShift, getResources().getInteger(R.integer.default_start_night_shift));
//            endTotalMinutes = sharedPreferences.getInt(mKeyEndNightShift, getResources().getInteger(R.integer.default_end_night_shift));
//        } else return;
//        Calendar start = Calendar.getInstance();
//        start.set(Calendar.HOUR_OF_DAY, TimePreference.calculateHours(startTotalMinutes));
//        start.set(Calendar.MINUTE, TimePreference.calculateMinutes(startTotalMinutes));
//        Calendar end = Calendar.getInstance();
//        end.set(Calendar.HOUR_OF_DAY, TimePreference.calculateHours(endTotalMinutes));
//        end.set(Calendar.MINUTE, TimePreference.calculateMinutes(endTotalMinutes));
//        String summary = getString(R.string.time_period_format, start, end);
//        preference.setSummary(summary);
//    }

}
