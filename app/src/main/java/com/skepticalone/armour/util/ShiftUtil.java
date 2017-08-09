package com.skepticalone.armour.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.data.util.ShiftData;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

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

    public enum ShiftType {
        NORMAL_DAY,
        LONG_DAY,
        NIGHT_SHIFT,
        CUSTOM
    }

    public static class Calculator {

        @Nullable
        private static Calculator CALCULATOR;
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

        @NonNull
        public static Calculator getInstance(@NonNull Context context) {
            if (CALCULATOR == null) {
                CALCULATOR = new Calculator(context.getApplicationContext());
            }
            return CALCULATOR;
        }

        private int getNormalDayTotalMinutes(boolean start) {
            return start ? mPreferences.getInt(normalDayStartKey, normalDayStartDefault) : mPreferences.getInt(normalDayEndKey, normalDayEndDefault);
        }
        private int getLongDayTotalMinutes(boolean start) {
            return start ? mPreferences.getInt(longDayStartKey, longDayStartDefault) : mPreferences.getInt(longDayEndKey, longDayEndDefault);
        }
        private int getNightShiftTotalMinutes(boolean start) {
            return start ? mPreferences.getInt(nightShiftStartKey, nightShiftStartDefault) : mPreferences.getInt(nightShiftEndKey, nightShiftEndDefault);
        }
        private boolean matches(int start1, int end1, int start2, int end2) {
            return start1 == start2 && end1 == end2;
        }
        private boolean isSingleNormalDay(@NonNull ShiftData shiftData) {
            return matches(shiftData.getStart().getMinuteOfDay(), shiftData.getEnd().getMinuteOfDay(), getNormalDayTotalMinutes(true), getNormalDayTotalMinutes(false));
        }
        private boolean isSingleLongDay(@NonNull ShiftData shiftData) {
            return matches(shiftData.getStart().getMinuteOfDay(), shiftData.getEnd().getMinuteOfDay(), getLongDayTotalMinutes(true), getLongDayTotalMinutes(false));
        }
        private boolean isSingleNightShift(@NonNull ShiftData shiftData) {
            return matches(shiftData.getStart().getMinuteOfDay(), shiftData.getEnd().getMinuteOfDay(), getNightShiftTotalMinutes(true), getNightShiftTotalMinutes(false));
        }
        @NonNull
        private <Entity extends Shift> List<Entity> getFilteredShifts(@NonNull List<Entity> allShifts, @NonNull ShiftFilter filter) {
            List<Entity> shifts = new ArrayList<>();
            for (Entity shift : allShifts) {
                if (filter.include(shift.getShiftData())) shifts.add(shift);
            }
            return shifts;
        }
        @NonNull
        public <Entity extends Shift> List<Entity> getFilteredShifts(@NonNull List<Entity> allShifts, @NonNull ShiftType shiftType) {
            final ShiftFilter filter;
            if (shiftType == ShiftType.NORMAL_DAY) {
                filter = new NormalDayFilter();
            } else if (shiftType == ShiftType.LONG_DAY) {
                filter = new LongDayFilter();
            } else if (shiftType == ShiftType.NIGHT_SHIFT) {
                filter = new NightShiftFilter();
            } else {
                filter = new CustomShiftFilter();
            }
            return getFilteredShifts(allShifts, filter);
        }
        @NonNull
        public <Entity extends Shift> List<Entity> getNormalDays(@NonNull List<Entity> allShifts) {
            return getFilteredShifts(allShifts, new NormalDayFilter());
        }
        @NonNull
        public <Entity extends Shift> List<Entity> getLongDays(@NonNull List<Entity> allShifts) {
            return getFilteredShifts(allShifts, new LongDayFilter());
        }
        @NonNull
        public <Entity extends Shift> List<Entity> getNightShifts(@NonNull List<Entity> allShifts) {
            return getFilteredShifts(allShifts, new NightShiftFilter());
        }
        @NonNull
        public <Entity extends Shift> List<Entity> getCustomShifts(@NonNull List<Entity> allShifts) {
            return getFilteredShifts(allShifts, new CustomShiftFilter());
        }
        @NonNull
        public ShiftType getSingleShiftType(@NonNull ShiftData shiftData) {
            if (isSingleNormalDay(shiftData)) return ShiftType.NORMAL_DAY;
            if (isSingleLongDay(shiftData)) return ShiftType.LONG_DAY;
            if (isSingleNightShift(shiftData)) return ShiftType.NIGHT_SHIFT;
            return ShiftType.CUSTOM;
        }

        @NonNull
        private LocalTime getStartOrEndTime(ShiftType shiftType, boolean start) {
            final int totalMinutes;
            switch (shiftType) {
                case NORMAL_DAY:
                    totalMinutes = getNormalDayTotalMinutes(start);
                    break;
                case LONG_DAY:
                    totalMinutes = getLongDayTotalMinutes(start);
                    break;
                case NIGHT_SHIFT:
                    totalMinutes = getNightShiftTotalMinutes(start);
                    break;
                default:
                    throw new IllegalStateException();
            }
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

        private abstract static class ShiftFilter {
            abstract boolean include(@NonNull ShiftData shiftData);
        }
        private abstract class IncludeFilter extends ShiftFilter {
            private final int startTotalMinutes, endTotalMinutes;
            IncludeFilter(int startTotalMinutes, int endTotalMinutes) {
                this.startTotalMinutes = startTotalMinutes;
                this.endTotalMinutes = endTotalMinutes;
            }
            @Override
            final boolean include(@NonNull ShiftData shiftData) {
                return matches(shiftData.getStart().getMinuteOfDay(), shiftData.getEnd().getMinuteOfDay(), startTotalMinutes, endTotalMinutes);
            }
        }
        private final class NormalDayFilter extends IncludeFilter {
            NormalDayFilter() {
                super(getNormalDayTotalMinutes(true), getNormalDayTotalMinutes(false));
            }
        }
        private final class LongDayFilter extends IncludeFilter {
            LongDayFilter() {
                super(getLongDayTotalMinutes(true), getLongDayTotalMinutes(false));
            }
        }
        private final class NightShiftFilter extends IncludeFilter {
            NightShiftFilter() {
                super(getNightShiftTotalMinutes(true), getNightShiftTotalMinutes(false));
            }
        }
        private final class CustomShiftFilter extends ShiftFilter {
            private final int
                    normalDayStartTotalMinutes = getNormalDayTotalMinutes(true),
                    normalDayEndTotalMinutes = getNormalDayTotalMinutes(false),
                    longDayStartTotalMinutes = getLongDayTotalMinutes(true),
                    longDayEndTotalMinutes = getLongDayTotalMinutes(false),
                    nightShiftStartTotalMinutes = getNightShiftTotalMinutes(true),
                    nightShiftEndTotalMinutes = getNightShiftTotalMinutes(false);
            @Override
            final boolean include(@NonNull ShiftData shiftData) {
                final int startTotalMinutes = shiftData.getStart().getMinuteOfDay(), endTotalMinutes = shiftData.getEnd().getMinuteOfDay();
                return !matches(startTotalMinutes, endTotalMinutes, normalDayStartTotalMinutes, normalDayEndTotalMinutes) &&
                        !matches(startTotalMinutes, endTotalMinutes, longDayStartTotalMinutes, longDayEndTotalMinutes) &&
                        !matches(startTotalMinutes, endTotalMinutes, nightShiftStartTotalMinutes, nightShiftEndTotalMinutes);
            }
        }
    }

}
