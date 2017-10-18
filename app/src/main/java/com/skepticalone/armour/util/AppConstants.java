package com.skepticalone.armour.util;

import android.support.annotation.NonNull;

import org.threeten.bp.Duration;

public final class AppConstants {

    public static final int
            MINIMUM_HOURS_BETWEEN_SHIFTS = 8,
            MAXIMUM_HOURS_OVER_DAY = 16,
            MAXIMUM_HOURS_OVER_WEEK = 72,
            MAXIMUM_HOURS_OVER_FORTNIGHT = 144,
            CATEGORY_A_HOURS = 65,
            CATEGORY_B_HOURS = 60,
            CATEGORY_C_HOURS = 55,
            CATEGORY_D_HOURS = 50,
            CATEGORY_E_HOURS = 45,
            CATEGORY_F_HOURS = 40;
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
