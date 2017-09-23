package com.skepticalone.armour.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import org.threeten.bp.ZoneId;

import java.util.TimeZone;

public final class TimeZoneListPreference extends ListPreference {

    public TimeZoneListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        String[] entries = TimeZone.getAvailableIDs();
        setEntryValues(entries);
        setEntries(entries);
        setDefaultValue(ZoneId.systemDefault().getId());
    }

    @Override
    public CharSequence getSummary() {
        return getValue();
    }
}