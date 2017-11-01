package com.skepticalone.armour.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.LiveConfig;

public final class LiveComplianceConfig extends LiveConfig<Configuration> {

    @Nullable
    private static LiveComplianceConfig INSTANCE;

    @NonNull
    private final String
            keySaferRosters,
            keyCheckDurationOverDay,
            keyCheckDurationOverWeek,
            keyCheckDurationOverFortnight,
            keyCheckDurationBetweenShifts,
            keyCheckLongDaysPerWeek,
            keyCheckConsecutiveDays,
            keyCheckConsecutiveNightsWorked,
            keyCheckRecoveryDaysFollowingNights,
            keyAllow1in2Weekends,
            keyCheckFrequencyOfWeekends,
            keyAllowFrequentConsecutiveWeekends,
            keyAllow5ConsecutiveNights,
            keyAllowOnly1RecoveryDayFollowing3Nights,
            keyAllowMidweekRDOs,
            keyCheckRDOs;

    private final boolean
            defaultSaferRosters,
            defaultCheckDurationOverDay,
            defaultCheckDurationOverWeek,
            defaultCheckDurationOverFortnight,
            defaultCheckDurationBetweenShifts,
            defaultCheckLongDaysPerWeek,
            defaultCheckConsecutiveDays,
            defaultCheckConsecutiveNightsWorked,
            defaultCheckRecoveryDaysFollowingNights,
            defaultAllow1in2Weekends,
            defaultCheckFrequencyOfWeekends,
            defaultAllowFrequentConsecutiveWeekends,
            defaultAllow5ConsecutiveNights,
            defaultAllowOnly1RecoveryDayFollowing3Nights,
            defaultAllowMidweekRDOs,
            defaultCheckRDOs;

    @NonNull
    private final String[] watchKeys;

    private LiveComplianceConfig(@NonNull Resources resources) {
        keySaferRosters = resources.getString(R.string.key_safer_rosters);
        keyCheckDurationOverDay = resources.getString(R.string.key_check_duration_over_day);
        keyCheckDurationOverWeek = resources.getString(R.string.key_check_duration_over_week);
        keyCheckDurationOverFortnight = resources.getString(R.string.key_check_duration_over_fortnight);
        keyCheckDurationBetweenShifts = resources.getString(R.string.key_check_duration_between_shifts);
        keyCheckLongDaysPerWeek = resources.getString(R.string.key_check_long_days_per_week);
        keyCheckConsecutiveDays = resources.getString(R.string.key_check_consecutive_days);
        keyCheckConsecutiveNightsWorked = resources.getString(R.string.key_check_consecutive_nights_worked);
        keyCheckRecoveryDaysFollowingNights = resources.getString(R.string.key_check_recovery_days_following_nights);
        keyAllow1in2Weekends = resources.getString(R.string.key_allow_1_in_2_weekends);
        keyCheckFrequencyOfWeekends = resources.getString(R.string.key_check_frequency_of_weekends);
        keyAllowFrequentConsecutiveWeekends = resources.getString(R.string.key_allow_frequent_consecutive_weekends);
        keyAllow5ConsecutiveNights = resources.getString(R.string.key_allow_5_consecutive_nights);
        keyAllowOnly1RecoveryDayFollowing3Nights = resources.getString(R.string.key_allow_only_1_recovery_day_following_3_nights);
        keyAllowMidweekRDOs = resources.getString(R.string.key_allow_midweek_rostered_days_off);
        keyCheckRDOs = resources.getString(R.string.key_check_rostered_days_off);
        defaultSaferRosters = resources.getBoolean(R.bool.default_safer_rosters);
        defaultCheckDurationOverDay = resources.getBoolean(R.bool.default_check_duration_over_day);
        defaultCheckDurationOverWeek = resources.getBoolean(R.bool.default_check_duration_over_week);
        defaultCheckDurationOverFortnight = resources.getBoolean(R.bool.default_check_duration_over_fortnight);
        defaultCheckDurationBetweenShifts = resources.getBoolean(R.bool.default_check_duration_between_shifts);
        defaultCheckLongDaysPerWeek = resources.getBoolean(R.bool.default_check_long_days_per_week);
        defaultCheckConsecutiveDays = resources.getBoolean(R.bool.default_check_consecutive_days);
        defaultCheckConsecutiveNightsWorked = resources.getBoolean(R.bool.default_check_consecutive_nights_worked);
        defaultCheckRecoveryDaysFollowingNights = resources.getBoolean(R.bool.default_check_recovery_days_following_nights);
        defaultAllow1in2Weekends = resources.getBoolean(R.bool.default_allow_1_in_2_weekends);
        defaultCheckFrequencyOfWeekends = resources.getBoolean(R.bool.default_check_frequency_of_weekends);
        defaultAllowFrequentConsecutiveWeekends = resources.getBoolean(R.bool.default_allow_frequent_consecutive_weekends);
        defaultAllow5ConsecutiveNights = resources.getBoolean(R.bool.default_allow_5_consecutive_nights);
        defaultAllowOnly1RecoveryDayFollowing3Nights = resources.getBoolean(R.bool.default_allow_only_1_recovery_day_following_3_nights);
        defaultAllowMidweekRDOs = resources.getBoolean(R.bool.default_allow_midweek_rostered_days_off);
        defaultCheckRDOs = resources.getBoolean(R.bool.default_check_rostered_days_off);
        watchKeys = new String[]{
                keySaferRosters,
                keyCheckDurationOverDay,
                keyCheckDurationOverWeek,
                keyCheckDurationOverFortnight,
                keyCheckDurationBetweenShifts,
                keyCheckLongDaysPerWeek,
                keyCheckConsecutiveDays,
                keyCheckConsecutiveNightsWorked,
                keyCheckRecoveryDaysFollowingNights,
                keyAllow1in2Weekends,
                keyCheckFrequencyOfWeekends,
                keyAllowFrequentConsecutiveWeekends,
                keyAllow5ConsecutiveNights,
                keyAllowOnly1RecoveryDayFollowing3Nights,
                keyAllowMidweekRDOs,
                keyCheckRDOs
        };
    }

    @NonNull
    public static LiveComplianceConfig getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (LiveComplianceConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveComplianceConfig(context.getResources());
                    INSTANCE.init(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public String[] getWatchKeys() {
        return watchKeys;
    }

    @NonNull
    @Override
    public Configuration getNewValue(@NonNull SharedPreferences sharedPreferences) {
        boolean
                checkDurationOverDay = sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
                checkDurationOverWeek = sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
                checkDurationOverFortnight = sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
                checkDurationBetweenShifts = sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
                checkLongDaysPerWeek = sharedPreferences.getBoolean(keyCheckLongDaysPerWeek, defaultCheckLongDaysPerWeek),
                checkConsecutiveDays = sharedPreferences.getBoolean(keyCheckConsecutiveDays, defaultCheckConsecutiveDays),
                checkConsecutiveNightsWorked = sharedPreferences.getBoolean(keyCheckConsecutiveNightsWorked, defaultCheckConsecutiveNightsWorked),
                checkRecoveryDaysFollowingNights = sharedPreferences.getBoolean(keyCheckRecoveryDaysFollowingNights, defaultCheckRecoveryDaysFollowingNights),
                allow1in2Weekends = sharedPreferences.getBoolean(keyAllow1in2Weekends, defaultAllow1in2Weekends),
                checkFrequencyOfWeekends = sharedPreferences.getBoolean(keyCheckFrequencyOfWeekends, defaultCheckFrequencyOfWeekends);
        return sharedPreferences.getBoolean(keySaferRosters, defaultSaferRosters) ?
                new ConfigurationSaferRosters(
                        checkDurationOverDay,
                        checkDurationOverWeek,
                        checkDurationOverFortnight,
                        checkDurationBetweenShifts,
                        checkLongDaysPerWeek,
                        checkConsecutiveDays,
                        checkConsecutiveNightsWorked,
                        checkRecoveryDaysFollowingNights,
                        allow1in2Weekends,
                        checkFrequencyOfWeekends,
                        sharedPreferences.getBoolean(keyAllowFrequentConsecutiveWeekends, defaultAllowFrequentConsecutiveWeekends),
                        sharedPreferences.getBoolean(keyAllow5ConsecutiveNights, defaultAllow5ConsecutiveNights),
                        sharedPreferences.getBoolean(keyAllowOnly1RecoveryDayFollowing3Nights, defaultAllowOnly1RecoveryDayFollowing3Nights),
                        sharedPreferences.getBoolean(keyAllowMidweekRDOs, defaultAllowMidweekRDOs),
                        sharedPreferences.getBoolean(keyCheckRDOs, defaultCheckRDOs)
                ) : new Configuration(
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkLongDaysPerWeek,
                checkConsecutiveDays,
                checkConsecutiveNightsWorked,
                checkRecoveryDaysFollowingNights,
                allow1in2Weekends,
                checkFrequencyOfWeekends
        );
    }

}
