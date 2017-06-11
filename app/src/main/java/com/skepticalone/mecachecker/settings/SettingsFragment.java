package com.skepticalone.mecachecker.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.BaseAdapter;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String mKeyStartNormalDay, mKeyEndNormalDay,
            mKeyStartLongDay, mKeyEndLongDay,
            mKeyStartNightShift, mKeyEndNightShift;
    private Preference mPreferenceNormalDay, mPreferenceLongDay, mPreferenceNightShift;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shift_preferences);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        mKeyStartNormalDay = getString(R.string.key_start_normal_day);
        mKeyEndNormalDay = getString(R.string.key_end_normal_day);
        mPreferenceNormalDay = getPreferenceScreen().findPreference(getString(R.string.key_normal_day));
        mPreferenceNormalDay.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartNormalDay, mKeyEndNormalDay));
        mKeyStartLongDay = getString(R.string.key_start_long_day);
        mKeyEndLongDay = getString(R.string.key_end_long_day);
        mPreferenceLongDay = getPreferenceScreen().findPreference(getString(R.string.key_long_day));
        mPreferenceLongDay.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartLongDay, mKeyEndLongDay));
        mKeyStartNightShift = getString(R.string.key_start_night_shift);
        mKeyEndNightShift = getString(R.string.key_end_night_shift);
        mPreferenceNightShift = getPreferenceScreen().findPreference(getString(R.string.key_night_shift));
        mPreferenceNightShift.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartNightShift, mKeyEndNightShift));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(mKeyStartNormalDay) || key.equals(mKeyEndNormalDay)) {
            mPreferenceNormalDay.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartNormalDay, mKeyEndNormalDay));
        } else if (key.equals(mKeyStartLongDay) || key.equals(mKeyEndLongDay)) {
            mPreferenceLongDay.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartLongDay, mKeyEndLongDay));
        } else if (key.equals(mKeyStartNightShift) || key.equals(mKeyEndNightShift)) {
            mPreferenceNightShift.setSummary(DateTimeUtils.getTimeSpanString(sharedPreferences, mKeyStartNightShift, mKeyEndNightShift));
        } else {
            return;
        }
        ((BaseAdapter) getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
    }

}
