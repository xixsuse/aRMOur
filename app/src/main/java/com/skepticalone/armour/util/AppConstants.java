package com.skepticalone.armour.util;

public final class AppConstants {

    public static final int
            MINIMUM_HOURS_BETWEEN_SHIFTS = 8,
            MAXIMUM_HOURS_OVER_DAY = 16,
            MAXIMUM_HOURS_OVER_WEEK = 72,
            MAXIMUM_HOURS_OVER_FORTNIGHT = 144,
            MAXIMUM_CONSECUTIVE_DAYS = 12,
            MAXIMUM_CONSECUTIVE_NIGHTS = 7,
            MINIMUM_NIGHTS_BEFORE_RECOVERY = 5,
            MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS = 2,
            MAXIMUM_HOURS_IN_NORMAL_DAY = 10,
            MAXIMUM_LONG_DAYS_PER_WEEK = 2,
            SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT = 4,
            SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT = 5,
            SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY = 2,
            SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS = 1,
            SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT = 1,
            SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT = 2,
            SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS = 2,
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
