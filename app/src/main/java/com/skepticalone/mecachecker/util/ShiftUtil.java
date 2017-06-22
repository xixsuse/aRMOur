package com.skepticalone.mecachecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class ShiftUtil {

    @StringRes
    public static int getShiftTypeTitle(ShiftType shiftType) {
        switch (shiftType) {
            case NORMAL_DAY:
                return R.string.normal_day;
            case LONG_DAY:
                return R.string.long_day;
            case NIGHT_SHIFT:
                return R.string.night_shift;
            case OTHER:
                return R.string.custom;
            default:
                throw new IllegalStateException();
        }
    }

    @DrawableRes
    public static int getShiftTypeIcon(ShiftType shiftType) {
        switch (shiftType) {
            case NORMAL_DAY:
                return R.drawable.ic_normal_day_black_24dp;
            case LONG_DAY:
                return R.drawable.ic_long_day_black_24dp;
            case NIGHT_SHIFT:
                return R.drawable.ic_night_shift_black_24dp;
            case OTHER:
                return R.drawable.ic_custom_shift_black_24dp;
            default:
                throw new IllegalStateException();
        }
    }

    @DrawableRes
    public static int getClaimStatusIcon(@NonNull Cursor cursor, int columnIndexClaimed, int columnIndexPaid) {
        return cursor.isNull(columnIndexPaid) ? cursor.isNull(columnIndexClaimed) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp;
    }

    public static class Calculator {

        private final SharedPreferences mPreferences;

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

        public Calculator(Context context) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            normalDayStartKey = context.getString(R.string.key_start_normal_day);
            normalDayEndKey = context.getString(R.string.key_end_normal_day);
            longDayStartKey = context.getString(R.string.key_start_long_day);
            longDayEndKey = context.getString(R.string.key_end_long_day);
            nightShiftStartKey = context.getString(R.string.key_start_night_shift);
            nightShiftEndKey = context.getString(R.string.key_end_night_shift);
            normalDayStartDefault = context.getResources().getInteger(R.integer.default_start_normal_day);
            normalDayEndDefault = context.getResources().getInteger(R.integer.default_end_normal_day);
            longDayStartDefault = context.getResources().getInteger(R.integer.default_start_long_day);
            longDayEndDefault = context.getResources().getInteger(R.integer.default_end_long_day);
            nightShiftStartDefault = context.getResources().getInteger(R.integer.default_start_night_shift);
            nightShiftEndDefault = context.getResources().getInteger(R.integer.default_end_night_shift);
        }

        public ShiftType getShiftType(Interval shift) {
            int startTotalMinutes = shift.getStart().getMinuteOfDay(),
                    endTotalMinutes = shift.getEnd().getMinuteOfDay();
            if (
                    startTotalMinutes == mPreferences.getInt(normalDayStartKey, normalDayStartDefault) &&
                            endTotalMinutes == mPreferences.getInt(normalDayEndKey, normalDayEndDefault)
                    ) {
                return ShiftType.NORMAL_DAY;
            } else if (
                    startTotalMinutes == mPreferences.getInt(longDayStartKey, longDayStartDefault) &&
                            endTotalMinutes == mPreferences.getInt(longDayEndKey, longDayEndDefault)
                    ) {
                return ShiftType.LONG_DAY;
            } else if (
                    startTotalMinutes == mPreferences.getInt(nightShiftStartKey, nightShiftStartDefault) &&
                            endTotalMinutes == mPreferences.getInt(nightShiftEndKey, nightShiftEndDefault)
                    ) {
                return ShiftType.NIGHT_SHIFT;
            } else {
                return ShiftType.OTHER;
            }
        }

        private LocalTime getStartOrEndTime(ShiftType shiftType, boolean start) {
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
            int totalMinutes = mPreferences.getInt(key, defaultTotalMinutes);
            return new LocalTime(DateTimeUtils.calculateHours(totalMinutes), DateTimeUtils.calculateMinutes(totalMinutes));
        }

        public LocalTime getStartTime(ShiftType shiftType) {
            return getStartOrEndTime(shiftType, true);
        }

        public LocalTime getEndTime(ShiftType shiftType) {
            return getStartOrEndTime(shiftType, false);
        }

    }

}
