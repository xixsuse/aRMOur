package com.skepticalone.armour.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.LiveConfiguration;

public final class LiveComplianceConfiguration extends LiveConfiguration<ComplianceConfiguration> {

    @Nullable
    private static LiveComplianceConfiguration INSTANCE;

    @NonNull
    private final String
            keySaferRosters,
            keyCheckDurationOverDay,
            keyCheckDurationOverWeek,
            keyCheckDurationOverFortnight,
            keyCheckDurationBetweenShifts,
            keyCheckLongDaysPerWeek,
            keyCheckConsecutiveDays,
            keyCheckConsecutiveNights,
            keyCheckRecoveryFollowingNights,
            key1in3Weekends,
            keyCheckConsecutiveWeekends,
            keyAllowFrequentConsecutiveWeekends,
            keyAllow5ConsecutiveNights,
            keyAllowOnly1RecoveryDayFollowing3Nights,
            keyAllowMidweekRDOs,
            keyCheckRosteredDaysOff;

    private final boolean
            defaultSaferRosters,
            defaultCheckDurationOverDay,
            defaultCheckDurationOverWeek,
            defaultCheckDurationOverFortnight,
            defaultCheckDurationBetweenShifts,
            defaultCheckLongDaysPerWeek,
            defaultCheckConsecutiveDays,
            defaultCheckConsecutiveNights,
            defaultCheckRecoveryFollowingNights,
            default1in3Weekends,
            defaultCheckConsecutiveWeekends,
            defaultAllowFrequentConsecutiveWeekends,
            defaultAllow5ConsecutiveNights,
            defaultAllowOnly1RecoveryDayFollowing3Nights,
            defaultAllowMidweekRDOs,
            defaultCheckRosteredDaysOff;

    @NonNull
    private final String[] watchKeys;

    private LiveComplianceConfiguration(@NonNull Resources resources) {
        key1in3Weekends = resources.getString(R.string.key_1_in_3_weekends);
        keySaferRosters = resources.getString(R.string.key_safer_rosters);
        keyAllowFrequentConsecutiveWeekends = resources.getString(R.string.key_allow_frequent_consecutive_weekends);
        keyAllow5ConsecutiveNights = resources.getString(R.string.key_allow_5_consecutive_nights);
        keyAllowOnly1RecoveryDayFollowing3Nights = resources.getString(R.string.key_allow_only_1_recovery_day_following_3_nights);
        keyAllowMidweekRDOs = resources.getString(R.string.key_allow_midweek_rostered_days_off);
        keyCheckDurationBetweenShifts = resources.getString(R.string.key_check_duration_between_shifts);
        keyCheckDurationOverDay = resources.getString(R.string.key_check_duration_over_day);
        keyCheckDurationOverWeek = resources.getString(R.string.key_check_duration_over_week);
        keyCheckDurationOverFortnight = resources.getString(R.string.key_check_duration_over_fortnight);
        keyCheckLongDaysPerWeek = resources.getString(R.string.key_check_long_days_per_week);
        keyCheckConsecutiveDays = resources.getString(R.string.key_check_consecutive_days);
        keyCheckConsecutiveWeekends = resources.getString(R.string.key_check_consecutive_weekends);
        keyCheckConsecutiveNights = resources.getString(R.string.key_check_consecutive_nights);
        keyCheckRecoveryFollowingNights = resources.getString(R.string.key_check_recovery_following_nights);
        keyCheckRosteredDaysOff = resources.getString(R.string.key_check_rostered_days_off);

        default1in3Weekends = resources.getBoolean(R.bool.default_1_in_3_weekends);
        defaultSaferRosters = resources.getBoolean(R.bool.default_safer_rosters);
        defaultAllowFrequentConsecutiveWeekends = resources.getBoolean(R.bool.default_allow_frequent_consecutive_weekends);
        defaultAllow5ConsecutiveNights = resources.getBoolean(R.bool.default_allow_5_consecutive_nights);
        defaultAllowOnly1RecoveryDayFollowing3Nights = resources.getBoolean(R.bool.default_allow_only_1_recovery_day_following_3_nights);
        defaultAllowMidweekRDOs = resources.getBoolean(R.bool.default_allow_midweek_rostered_days_off);
        defaultCheckDurationBetweenShifts = resources.getBoolean(R.bool.default_check_duration_between_shifts);
        defaultCheckDurationOverDay = resources.getBoolean(R.bool.default_check_duration_over_day);
        defaultCheckDurationOverWeek = resources.getBoolean(R.bool.default_check_duration_over_week);
        defaultCheckDurationOverFortnight = resources.getBoolean(R.bool.default_check_duration_over_fortnight);
        defaultCheckLongDaysPerWeek = resources.getBoolean(R.bool.default_check_long_days_per_week);
        defaultCheckConsecutiveDays = resources.getBoolean(R.bool.default_check_consecutive_days);
        defaultCheckConsecutiveWeekends = resources.getBoolean(R.bool.default_check_consecutive_weekends);
        defaultCheckConsecutiveNights = resources.getBoolean(R.bool.default_check_consecutive_nights);
        defaultCheckRecoveryFollowingNights = resources.getBoolean(R.bool.default_check_recovery_following_nights);
        defaultCheckRosteredDaysOff = resources.getBoolean(R.bool.default_check_rostered_days_off);
        watchKeys = new String[]{
                key1in3Weekends,
                keySaferRosters,
                keyAllowFrequentConsecutiveWeekends,
                keyAllow5ConsecutiveNights,
                keyAllowOnly1RecoveryDayFollowing3Nights,
                keyAllowMidweekRDOs,
                keyCheckDurationBetweenShifts,
                keyCheckDurationOverDay,
                keyCheckDurationOverWeek,
                keyCheckDurationOverFortnight,
                keyCheckLongDaysPerWeek,
                keyCheckConsecutiveDays,
                keyCheckConsecutiveWeekends,
                keyCheckConsecutiveNights,
                keyCheckRecoveryFollowingNights,
                keyCheckRosteredDaysOff,
        };
    }

    @NonNull
    public static LiveComplianceConfiguration getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (LiveComplianceConfiguration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveComplianceConfiguration(context.getResources());
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
    public ComplianceConfiguration getNewValue(@NonNull SharedPreferences sharedPreferences) {
        boolean
                _1in3Weekends = sharedPreferences.getBoolean(key1in3Weekends, default1in3Weekends),
                checkDurationBetweenShifts = sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
                checkDurationOverDay = sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
                checkDurationOverWeek = sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
                checkDurationOverFortnight = sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
                checkLongDaysPerWeek = sharedPreferences.getBoolean(keyCheckLongDaysPerWeek, defaultCheckLongDaysPerWeek),
                checkConsecutiveDays = sharedPreferences.getBoolean(keyCheckConsecutiveDays, defaultCheckConsecutiveDays),
                checkConsecutiveNights = sharedPreferences.getBoolean(keyCheckConsecutiveNights, defaultCheckConsecutiveNights),
                checkConsecutiveWeekends = sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends),
                checkRecoveryFollowingNights = sharedPreferences.getBoolean(keyCheckRecoveryFollowingNights, defaultCheckRecoveryFollowingNights);
        return sharedPreferences.getBoolean(keySaferRosters, defaultSaferRosters) ?
                new ComplianceConfigurationSaferRosters(
                        _1in3Weekends,
                        sharedPreferences.getBoolean(keyAllowFrequentConsecutiveWeekends, defaultAllowFrequentConsecutiveWeekends),
                        sharedPreferences.getBoolean(keyAllow5ConsecutiveNights, defaultAllow5ConsecutiveNights),
                        sharedPreferences.getBoolean(keyAllowOnly1RecoveryDayFollowing3Nights, defaultAllowOnly1RecoveryDayFollowing3Nights),
                        sharedPreferences.getBoolean(keyAllowMidweekRDOs, defaultAllowMidweekRDOs),
                        checkDurationBetweenShifts,
                        checkDurationOverDay,
                        checkDurationOverWeek,
                        checkDurationOverFortnight,
                        checkLongDaysPerWeek,
                        checkConsecutiveDays,
                        checkConsecutiveWeekends,
                        checkConsecutiveNights,
                        checkRecoveryFollowingNights,
                        sharedPreferences.getBoolean(keyCheckRosteredDaysOff, defaultCheckRosteredDaysOff)
                ) :
                new ComplianceConfiguration(
                        _1in3Weekends,
                        checkDurationBetweenShifts,
                        checkDurationOverDay,
                        checkDurationOverWeek,
                        checkDurationOverFortnight,
                        checkLongDaysPerWeek,
                        checkConsecutiveDays,
                        checkConsecutiveWeekends,
                        checkConsecutiveNights,
                        checkRecoveryFollowingNights
                );
    }

}
