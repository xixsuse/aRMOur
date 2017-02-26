package com.skepticalone.mecachecker.util;

public final class AppConstants {

    public static final int
            MINIMUM_REST_HOURS = 8,
            MINUTES_PER_HOUR = 60;
    private static final int
            MINUTES_PER_STEP = 5,
            MILLIS_PER_SECOND = 1000,
            SECONDS_PER_MINUTE = 60,
            MAXIMUM_HOURS_PER_DAY = 16,
            MAXIMUM_HOURS_PER_WEEK = 72,
            MAXIMUM_HOURS_PER_FORTNIGHT = 144;
    static final int
            MILLIS_PER_MINUTE = MILLIS_PER_SECOND * SECONDS_PER_MINUTE,
            MILLIS_PER_HOUR = MILLIS_PER_MINUTE * MINUTES_PER_HOUR,
            MILLIS_PER_STEP = MILLIS_PER_MINUTE * MINUTES_PER_STEP,
            STEPS_PER_HOUR = MINUTES_PER_HOUR / MINUTES_PER_STEP;
    public static final long
            MINIMUM_DURATION_REST = MINIMUM_REST_HOURS * MILLIS_PER_HOUR,
            MAXIMUM_DURATION_OVER_DAY = MAXIMUM_HOURS_PER_DAY * MILLIS_PER_HOUR,
            MAXIMUM_DURATION_OVER_WEEK = MAXIMUM_HOURS_PER_WEEK * MILLIS_PER_HOUR,
            MAXIMUM_DURATION_OVER_FORTNIGHT = MAXIMUM_HOURS_PER_FORTNIGHT * MILLIS_PER_HOUR;

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % AppConstants.MINUTES_PER_STEP;
    }
}
