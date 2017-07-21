package com.skepticalone.mecachecker.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.Duration;

public final class AppConstants {

    private static final int MINIMUM_HOURS_BETWEEN_SHIFTS = 8;
    private static final Duration MINIMUM_DURATION_BETWEEN_SHIFTS = Duration.standardHours(MINIMUM_HOURS_BETWEEN_SHIFTS);
    private static final int MAXIMUM_HOURS_OVER_DAY = 16;
    private static final int MAXIMUM_HOURS_OVER_WEEK = 72;
    private static final int MAXIMUM_HOURS_OVER_FORTNIGHT = 144;
    private static final Duration MAXIMUM_DURATION_OVER_DAY = Duration.standardHours(MAXIMUM_HOURS_OVER_DAY);
    private static final Duration MAXIMUM_DURATION_OVER_WEEK = Duration.standardHours(MAXIMUM_HOURS_OVER_WEEK);
    private static final Duration MAXIMUM_DURATION_OVER_FORTNIGHT = Duration.standardHours(MAXIMUM_HOURS_OVER_FORTNIGHT);
    private static final int MINUTES_PER_STEP = 5;

    private AppConstants() {
    }

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean insufficientDurationBetweenShifts(@Nullable Duration duration) {
        return duration != null && duration.isShorterThan(AppConstants.MINIMUM_DURATION_BETWEEN_SHIFTS);
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
