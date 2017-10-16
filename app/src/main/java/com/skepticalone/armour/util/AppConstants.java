package com.skepticalone.armour.util;

import android.support.annotation.NonNull;

import org.threeten.bp.Duration;

public final class AppConstants {

    public static final int MINIMUM_HOURS_BETWEEN_SHIFTS = 8;
    public static final int MAXIMUM_HOURS_OVER_DAY = 16;
    public static final int MAXIMUM_HOURS_OVER_WEEK = 72;
    public static final int MAXIMUM_HOURS_OVER_FORTNIGHT = 144;
    private static final int MINUTES_PER_STEP = 5;

    private AppConstants() {
    }

    public static int getSteppedMinutes(int minutes) {
        return minutes - minutes % MINUTES_PER_STEP;
    }

    public static boolean sufficientDurationBetweenShifts(@NonNull Duration duration) {
        return duration.compareTo(Duration.ofHours(MINIMUM_HOURS_BETWEEN_SHIFTS)) != -1;
    }

    public static boolean compliesWithMaximumDurationOverDay(@NonNull Duration duration) {
        return duration.compareTo(Duration.ofHours(MAXIMUM_HOURS_OVER_DAY)) != 1;
    }

    public static boolean compliesWithMaximumDurationOverWeek(@NonNull Duration duration) {
        return duration.compareTo(Duration.ofHours(MAXIMUM_HOURS_OVER_WEEK)) != 1;
    }

    public static boolean compliesWithMaximumDurationOverFortnight(@NonNull Duration duration) {
        return duration.compareTo(Duration.ofHours(MAXIMUM_HOURS_OVER_FORTNIGHT)) != 1;
    }

}
