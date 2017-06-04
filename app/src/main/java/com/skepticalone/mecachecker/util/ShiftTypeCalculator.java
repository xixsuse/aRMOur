package com.skepticalone.mecachecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class ShiftTypeCalculator {

    private final String
            normalDayStartKey,
            normalDayEndKey,
            longDayStartKey,
            longDayEndKey,
            nightShiftStartKey,
            nightShiftEndKey;

    private final int
            normalDayStartDefault,
            normalDayEndDefault,
            longDayStartDefault,
            longDayEndDefault,
            nightShiftStartDefault,
            nightShiftEndDefault;

    public ShiftTypeCalculator(Resources resources) {
        normalDayStartKey = resources.getString(R.string.key_start_normal_day);
        normalDayEndKey = resources.getString(R.string.key_end_normal_day);
        longDayStartKey = resources.getString(R.string.key_start_long_day);
        longDayEndKey = resources.getString(R.string.key_end_long_day);
        nightShiftStartKey = resources.getString(R.string.key_start_night_shift);
        nightShiftEndKey = resources.getString(R.string.key_end_night_shift);
        normalDayStartDefault = resources.getInteger(R.integer.default_start_normal_day);
        normalDayEndDefault = resources.getInteger(R.integer.default_end_normal_day);
        longDayStartDefault = resources.getInteger(R.integer.default_start_long_day);
        longDayEndDefault = resources.getInteger(R.integer.default_end_long_day);
        nightShiftStartDefault = resources.getInteger(R.integer.default_start_night_shift);
        nightShiftEndDefault = resources.getInteger(R.integer.default_end_night_shift);
    }

    public ShiftType getShiftType(Interval shift, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int startTotalMinutes = shift.getStart().getMinuteOfDay(),
                endTotalMinutes = shift.getEnd().getMinuteOfDay();
        if (
                startTotalMinutes == preferences.getInt(normalDayStartKey, normalDayStartDefault) &&
                        endTotalMinutes == preferences.getInt(normalDayEndKey, normalDayEndDefault)
                ) {
            return ShiftType.NORMAL_DAY;
        } else if (
                startTotalMinutes == preferences.getInt(longDayStartKey, longDayStartDefault) &&
                        endTotalMinutes == preferences.getInt(longDayEndKey, longDayEndDefault)
                ) {
            return ShiftType.LONG_DAY;
        } else if (
                startTotalMinutes == preferences.getInt(nightShiftStartKey, nightShiftStartDefault) &&
                        endTotalMinutes == preferences.getInt(nightShiftEndKey, nightShiftEndDefault)
                ) {
            return ShiftType.NIGHT_SHIFT;
        } else {
            return ShiftType.OTHER;
        }
    }

    private LocalTime getStartOrEndTime(String key, int defaultTotalMinutes, SharedPreferences preferences) {
        int totalMinutes = preferences.getInt(key, defaultTotalMinutes);
        return new LocalTime(DateTimeUtils.calculateHours(totalMinutes), DateTimeUtils.calculateMinutes(totalMinutes));
    }

    private LocalTime getStartOrEndTime(ShiftType shiftType, boolean start, SharedPreferences preferences) {
        String key;
        int defaultTotalMinutes;
        switch (shiftType) {
            case NORMAL_DAY:
                key = start ? normalDayStartKey : normalDayEndKey;
                defaultTotalMinutes = start ? normalDayStartDefault : normalDayEndDefault;
                break;
            case LONG_DAY:
                key = start ? longDayStartKey : longDayEndKey;
                defaultTotalMinutes = start ? longDayStartDefault : longDayEndDefault;
                break;
            case NIGHT_SHIFT:
                key = start ? nightShiftStartKey : nightShiftEndKey;
                defaultTotalMinutes = start ? nightShiftStartDefault : nightShiftEndDefault;
                break;
            default:
                throw new IllegalStateException();
        }
        return getStartOrEndTime(key, defaultTotalMinutes, preferences);
    }

    public LocalTime getStartTime(ShiftType shiftType, SharedPreferences preferences) {
        return getStartOrEndTime(shiftType, true, preferences);
    }

    public LocalTime getEndTime(ShiftType shiftType, SharedPreferences preferences) {
        return getStartOrEndTime(shiftType, false, preferences);
    }

}
