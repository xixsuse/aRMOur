package com.skepticalone.mecachecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalTime;

public final class ShiftUtil {

    @StringRes
    public static int getShiftTitle(ShiftType shiftType) {
        if (shiftType == ShiftType.NORMAL_DAY) {
            return R.string.normal_day;
        } else if (shiftType == ShiftType.LONG_DAY) {
            return R.string.long_day;
        } else if (shiftType == ShiftType.NIGHT_SHIFT) {
            return R.string.night_shift;
        } else {
            return R.string.custom_shift;
        }
    }

    @DrawableRes
    public static int getShiftIcon(ShiftType shiftType) {
        if (shiftType == ShiftType.NORMAL_DAY) {
            return R.drawable.ic_normal_day_black_24dp;
        } else if (shiftType == ShiftType.LONG_DAY) {
            return R.drawable.ic_long_day_black_24dp;
        } else if (shiftType == ShiftType.NIGHT_SHIFT) {
            return R.drawable.ic_night_shift_black_24dp;
        } else {
            return R.drawable.ic_custom_shift_black_24dp;
        }
    }

    public static class Calculator {

        @Nullable
        private static Calculator CALCULATOR;

        @NonNull
        public static Calculator getInstance(@NonNull Context context) {
            if (CALCULATOR == null) {
                CALCULATOR = new Calculator(context.getApplicationContext());
            }
            return CALCULATOR;
        }

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

        private Calculator(@NonNull Context applicationContext) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            normalDayStartKey = applicationContext.getString(R.string.key_start_normal_day);
            normalDayEndKey = applicationContext.getString(R.string.key_end_normal_day);
            longDayStartKey = applicationContext.getString(R.string.key_start_long_day);
            longDayEndKey = applicationContext.getString(R.string.key_end_long_day);
            nightShiftStartKey = applicationContext.getString(R.string.key_start_night_shift);
            nightShiftEndKey = applicationContext.getString(R.string.key_end_night_shift);
            normalDayStartDefault = applicationContext.getResources().getInteger(R.integer.default_start_normal_day);
            normalDayEndDefault = applicationContext.getResources().getInteger(R.integer.default_end_normal_day);
            longDayStartDefault = applicationContext.getResources().getInteger(R.integer.default_start_long_day);
            longDayEndDefault = applicationContext.getResources().getInteger(R.integer.default_end_long_day);
            nightShiftStartDefault = applicationContext.getResources().getInteger(R.integer.default_start_night_shift);
            nightShiftEndDefault = applicationContext.getResources().getInteger(R.integer.default_end_night_shift);
        }
//
//        @Override
//        public final int getShiftIcon(@NonNull ShiftData shiftData) {
//            return ShiftUtil.getShiftIcon(getShiftType(shiftData));
//        }
//
//        @Override
//        public int getShiftTitle(@NonNull ShiftData shiftData) {
//            return ShiftUtil.getShiftTitle(getShiftType(shiftData));
//        }

        @NonNull
        public ShiftType getShiftType(@NonNull ShiftData shiftData) {
            int startTotalMinutes = shiftData.getStart().getMinuteOfDay(),
                    endTotalMinutes = shiftData.getEnd().getMinuteOfDay();
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

        @NonNull
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

        @NonNull
        public LocalTime getStartTime(ShiftType shiftType) {
            return getStartOrEndTime(shiftType, true);
        }

        @NonNull
        public LocalTime getEndTime(ShiftType shiftType) {
            return getStartOrEndTime(shiftType, false);
        }

    }

    public enum ShiftType {
        NORMAL_DAY,
        LONG_DAY,
        NIGHT_SHIFT,
        OTHER
    }
}
