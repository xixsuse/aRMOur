package com.skepticalone.armour.util;

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

}
