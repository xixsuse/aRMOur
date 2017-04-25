package com.skepticalone.mecachecker.util;

import android.support.annotation.Nullable;

import org.joda.time.Duration;
import org.joda.time.Interval;

public final class AppConstants {


    public static final int MINIMUM_HOURS_BETWEEN_SHIFTS = 8;
    public static final int MAXIMUM_HOURS_OVER_DAY = 16;
    public static final int MAXIMUM_HOURS_OVER_WEEK = 72;
    public static final int MAXIMUM_HOURS_OVER_FORTNIGHT = 144;
    public static final Duration MINIMUM_DURATION_BETWEEN_SHIFTS = Duration.standardHours(MINIMUM_HOURS_BETWEEN_SHIFTS);
    private static final Duration MAXIMUM_DURATION_OVER_DAY = Duration.standardHours(MAXIMUM_HOURS_OVER_DAY);
    private static final Duration MAXIMUM_DURATION_OVER_WEEK = Duration.standardHours(MAXIMUM_HOURS_OVER_WEEK);
    private static final Duration MAXIMUM_DURATION_OVER_FORTNIGHT = Duration.standardHours(MAXIMUM_HOURS_OVER_FORTNIGHT);
    private static final int MINUTES_PER_STEP = 5;
    private static final int SHIFT_TYPE_ROSTERED = 1, SHIFT_TYPE_ADDITIONAL = 2;

    private AppConstants() {
    }

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean hasInsufficientIntervalBetweenShifts(@Nullable Interval interval) {
        return interval != null && interval.toDuration().isShorterThan(AppConstants.MINIMUM_DURATION_BETWEEN_SHIFTS);
    }

    public static boolean exceedsDurationOverDay(Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_DAY);
    }

    public static boolean exceedsDurationOverWeek(Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_WEEK);
    }

    public static boolean exceedsDurationOverFortnight(Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_FORTNIGHT);
    }

}
