package com.skepticalone.mecachecker.util;

import android.support.annotation.Nullable;

import org.joda.time.Duration;
import org.joda.time.Interval;

public final class AppConstants {

    public static final Duration MINIMUM_TIME_BETWEEN_SHIFTS = Duration.standardHours(8);
    private static final Duration MAXIMUM_DURATION_OVER_DAY = Duration.standardHours(16);
    private static final Duration MAXIMUM_DURATION_OVER_WEEK = Duration.standardHours(72);
    private static final Duration MAXIMUM_DURATION_OVER_FORTNIGHT = Duration.standardHours(144);
    private static final int MINUTES_PER_STEP = 5;
    private static final int SHIFT_TYPE_ROSTERED = 1, SHIFT_TYPE_ADDITIONAL = 2;

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean hasInsufficientIntervalBetweenShifts(@Nullable Interval interval) {
        return interval != null && interval.toDuration().isShorterThan(AppConstants.MINIMUM_TIME_BETWEEN_SHIFTS);
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
