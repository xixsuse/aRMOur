package com.skepticalone.mecachecker.util;

import android.support.annotation.Nullable;

import org.joda.time.Duration;

public final class AppConstants {

    public static final Duration MINIMUM_TIME_BETWEEN_SHIFTS = Duration.standardHours(8);
    private static final Duration MAXIMUM_DURATION_OVER_DAY = Duration.standardHours(16);
    private static final Duration MAXIMUM_DURATION_OVER_WEEK = Duration.standardHours(72);
    private static final Duration MAXIMUM_DURATION_OVER_FORTNIGHT = Duration.standardHours(144);
    private static final int MINUTES_PER_STEP = 5;

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean hasInsufficientTimeBetweenShifts(@Nullable Duration duration) {
        return duration != null && duration.isShorterThan(AppConstants.MINIMUM_TIME_BETWEEN_SHIFTS);
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
