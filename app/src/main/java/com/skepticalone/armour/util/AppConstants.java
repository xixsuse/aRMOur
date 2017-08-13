package com.skepticalone.armour.util;

import android.support.annotation.NonNull;

import org.joda.time.Duration;

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

    private AppConstants() {
    }

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean insufficientDurationBetweenShifts(@NonNull Duration duration) {
        return duration.isShorterThan(AppConstants.MINIMUM_DURATION_BETWEEN_SHIFTS);
    }

    public static boolean exceedsMaximumDurationOverDay(@NonNull Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_DAY);
    }

    public static boolean exceedsMaximumDurationOverWeek(@NonNull Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_WEEK);
    }

    public static boolean exceedsMaximumDurationOverFortnight(@NonNull Duration duration) {
        return duration.isLongerThan(MAXIMUM_DURATION_OVER_FORTNIGHT);
    }

}
