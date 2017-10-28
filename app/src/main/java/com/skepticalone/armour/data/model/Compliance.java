package com.skepticalone.armour.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.compliance.Configuration;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.LiveConfig;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class Compliance {

    @NonNull
    private final Duration durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    private final Duration durationBetweenShifts;
    @Nullable
    private final Integer indexOfDay, indexOfLongDay, indexOfNightShift;
    @Nullable
    private final Weekend weekend;
    @Nullable
    private final RecoveryInformation recoveryInformation;
    @Nullable
    private final Boolean
            compliesWithMaximumDurationOverDay,
            compliesWithMaximumDurationOverWeek,
            compliesWithMaximumDurationOverFortnight,
            sufficientDurationBetweenShifts,
            compliesWithMaximumDaysPerWeek,
            compliesWithMaximumLongDaysPerWeek,
            compliesWithMaximumConsecutiveNightsWorked,
            sufficientRecoveryDaysFollowingNights;
    private boolean compliant;
    //    @Nullable
//    private LocalDate rosteredDayOff;
    @Nullable
    private Boolean compliesWithRosteredDayOff;

    @SuppressWarnings("ConstantConditions")
    Compliance(
            @NonNull Shift.Data shiftData,
            @NonNull List<RosteredShift> previousShifts,
            @NonNull Configuration configuration
    ) {
        durationOverDay = getDurationSince(shiftData, shiftData.getEnd().minusDays(1), previousShifts);
        compliesWithMaximumDurationOverDay = configuration.checkDurationOverDay ? durationOverDay.compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_OVER_DAY)) != 1 : null;
        durationOverWeek = getDurationSince(shiftData, shiftData.getEnd().minusWeeks(1), previousShifts);
        compliesWithMaximumDurationOverWeek = configuration.checkDurationOverWeek ? durationOverWeek.compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_OVER_WEEK)) != 1 : null;
        durationOverFortnight = getDurationSince(shiftData, shiftData.getEnd().minusWeeks(2), previousShifts);
        compliesWithMaximumDurationOverFortnight = configuration.checkDurationOverFortnight ? durationOverFortnight.compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT)) != 1 : null;
        RosteredShift previousShift = previousShifts.isEmpty() ? null : previousShifts.get(previousShifts.size() - 1);
        durationBetweenShifts = previousShift == null ? null : Duration.between(previousShift.getShiftData().getEnd(), shiftData.getStart());
        sufficientDurationBetweenShifts = configuration.checkDurationBetweenShifts && durationBetweenShifts != null ? durationBetweenShifts.compareTo(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)) != -1 : null;

        weekend = Weekend.from(shiftData, previousShifts, configuration);

        indexOfNightShift = getIndexOfNightShift(shiftData, previousShift);
        compliesWithMaximumConsecutiveNightsWorked = configuration.checkConsecutiveNightsWorked && indexOfNightShift != null ? indexOfNightShift < (configuration.saferRostersOptions == null ? AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS : configuration.saferRostersOptions.allow5ConsecutiveNights ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT) : null;
        recoveryInformation = indexOfNightShift == null && previousShift != null && previousShift.getCompliance().indexOfNightShift != null ? new RecoveryInformation(previousShift.getCompliance().indexOfNightShift, shiftData.getStart().toLocalDate(), previousShift.getShiftData().getEnd().toLocalDate()) : null;
        sufficientRecoveryDaysFollowingNights = configuration.checkRecoveryDaysFollowingNights && recoveryInformation != null ? recoveryInformation.previousConsecutiveNightShifts < (configuration.saferRostersOptions == null ? AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY : AppConstants.SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY) || recoveryInformation.recoveryDaysFollowingNights >= (configuration.saferRostersOptions == null ? AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS : getMinimumRecoveryDaysFollowingNightsSaferRosters(recoveryInformation.previousConsecutiveNightShifts, configuration.saferRostersOptions.allowOnly1RecoveryDayFollowing3Nights)) : null;
        indexOfDay = indexOfNightShift == null ? getIndexOfDay(shiftData, previousShift) : null;
        compliesWithMaximumDaysPerWeek = configuration.checkConsecutiveDays && indexOfDay != null ? indexOfDay < (configuration.saferRostersOptions == null ? AppConstants.MAXIMUM_CONSECUTIVE_DAYS : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_DAYS) : null;
        indexOfLongDay = indexOfNightShift == null ? getIndexOfLongDay(shiftData, previousShifts) : null;
        compliesWithMaximumLongDaysPerWeek = configuration.checkLongDaysPerWeek && indexOfLongDay != null ? indexOfLongDay < AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK : null;
        compliant = (compliesWithMaximumDurationOverDay == null || compliesWithMaximumDurationOverDay) &&
                (compliesWithMaximumDurationOverWeek == null || compliesWithMaximumDurationOverWeek) &&
                (compliesWithMaximumDurationOverFortnight == null || compliesWithMaximumDurationOverFortnight) &&
                (sufficientDurationBetweenShifts == null || sufficientDurationBetweenShifts) &&
                (compliesWithMaximumDaysPerWeek == null || compliesWithMaximumDaysPerWeek) &&
                (compliesWithMaximumLongDaysPerWeek == null || compliesWithMaximumLongDaysPerWeek) &&
                (weekend == null || weekend.compliesWithMaximumWeekendsWorked == null || weekend.compliesWithMaximumWeekendsWorked) &&
                (compliesWithMaximumConsecutiveNightsWorked == null || compliesWithMaximumConsecutiveNightsWorked) &&
                (sufficientRecoveryDaysFollowingNights == null || sufficientRecoveryDaysFollowingNights);
    }

    private static int getMinimumRecoveryDaysFollowingNightsSaferRosters(long previousConsecutiveNightShifts, boolean allowOnly1RecoveryDayFollowing3Nights) {
        return previousConsecutiveNightShifts < 3 ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS :
                previousConsecutiveNightShifts > 3 ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS :
                        allowOnly1RecoveryDayFollowing3Nights ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT;
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
    }

    @NonNull
    private static Duration getDurationAfterCutoff(@NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff) {
        return shiftData.getStart().isBefore(cutOff) ? Duration.between(cutOff, shiftData.getEnd()) : shiftData.getDuration();
    }

    @NonNull
    private static Duration getDurationSince(@NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff, @NonNull List<RosteredShift> previousShifts) {
        Duration totalDuration = getDurationAfterCutoff(shiftData, cutOff);
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            Shift.Data pastShiftData = previousShifts.get(i).getShiftData();
            if (!pastShiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(getDurationAfterCutoff(pastShiftData, cutOff));
        }
        return totalDuration;
    }

    @Nullable
    private static Integer getIndexOfNightShift(@NonNull Shift.Data shiftData, @Nullable RosteredShift previousShift) {
        LocalDate shiftEnd = shiftData.getEnd().toLocalDate();
        if (!shiftData.getStart().toLocalDate().isEqual(shiftEnd) || shiftData.getStart().toLocalTime().isBefore(LocalTime.of(6, 0))) {
            if (previousShift != null && previousShift.getCompliance().getIndexOfNightShift() != null) {
                long differenceInNights = shiftEnd.toEpochDay() - previousShift.getShiftData().getEnd().toLocalDate().toEpochDay();
                if (differenceInNights <= 1) {
                    return previousShift.getCompliance().getIndexOfNightShift() + (int) differenceInNights;
                }
            }
            return 0;
        }
        return null;
    }

    private static int getIndexOfDay(@NonNull Shift.Data shiftData, @Nullable RosteredShift previousShift) {
        if (previousShift != null && previousShift.getCompliance().getIndexOfDay() != null) {
            long differenceInDays = shiftData.getStart().toLocalDate().toEpochDay() - previousShift.getShiftData().getStart().toLocalDate().toEpochDay();
            if (differenceInDays <= 1) {
                return previousShift.getCompliance().getIndexOfDay() + (int) differenceInDays;
            }
        }
        return 0;
    }

    @Nullable
    private static Integer getIndexOfLongDay(@NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        if (shiftData.getDuration().compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY)) == 1) {
            final LocalDate oneWeekAgo = shiftData.getStart().toLocalDate().minusWeeks(1);
            int index = 0;
            for (int i = previousShifts.size() - 1; i >= 0; i--) {
                RosteredShift previousShift = previousShifts.get(i);
                if (!previousShift.getShiftData().getStart().toLocalDate().isAfter(oneWeekAgo))
                    break;
                else if (previousShift.getCompliance().getIndexOfLongDay() != null) index++;
            }
            return index;
        } else return null;
    }

    @NonNull
    public Duration getDurationOverDay() {
        return durationOverDay;
    }

    @Nullable
    public Boolean compliesWithMaximumDurationOverDay() {
        return compliesWithMaximumDurationOverDay;
    }

    @NonNull
    public Duration getDurationOverWeek() {
        return durationOverWeek;
    }

    @Nullable
    public Boolean compliesWithMaximumDurationOverWeek() {
        return compliesWithMaximumDurationOverWeek;
    }

    @NonNull
    public Duration getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    public Boolean compliesWithMaximumDurationOverFortnight() {
        return compliesWithMaximumDurationOverFortnight;
    }

    @Nullable
    public Duration getDurationBetweenShifts() {
        return durationBetweenShifts;
    }

    @Nullable
    public Boolean sufficientDurationBetweenShifts() {
        return sufficientDurationBetweenShifts;
    }

    @Nullable
    public Integer getIndexOfDay() {
        return indexOfDay;
    }

    @Nullable
    public Boolean compliesWithMaximumDaysPerWeek() {
        return compliesWithMaximumDaysPerWeek;
    }

    @Nullable
    public Integer getIndexOfLongDay() {
        return indexOfLongDay;
    }

    @Nullable
    public Boolean compliesWithMaximumLongDaysPerWeek() {
        return compliesWithMaximumLongDaysPerWeek;
    }

    @Nullable
    public Integer getIndexOfNightShift() {
        return indexOfNightShift;
    }

    @Nullable
    public Boolean compliesWithMaximumConsecutiveNightsWorked() {
        return compliesWithMaximumConsecutiveNightsWorked;
    }

    @Nullable
    public Weekend getWeekend() {
        return weekend;
    }

    @Nullable
    public RecoveryInformation getRecoveryInformation() {
        return recoveryInformation;
    }

    @Nullable
    public Boolean sufficientRecoveryFollowingNights() {
        return sufficientRecoveryDaysFollowingNights;
    }

    //    @Nullable
//    LocalDate getRosteredDayOff() {
//        return rosteredDayOff;
//    }
//
    @Nullable
    public Boolean compliesWithRosteredDayOff() {
        return compliesWithRosteredDayOff;
    }

    void setNonCompliant() {
        this.compliant = false;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public static final class RecoveryInformation {
        public final int previousConsecutiveNightShifts, recoveryDaysFollowingNights;

        private RecoveryInformation(int indexOfPreviousNightShift, @NonNull LocalDate start, @NonNull LocalDate previousShiftEnd) {
            previousConsecutiveNightShifts = indexOfPreviousNightShift + 1;
            recoveryDaysFollowingNights = start.isEqual(previousShiftEnd) ? 0 : (int) (start.toEpochDay() - previousShiftEnd.toEpochDay() - 1);
        }

        public boolean isEqualTo(@NonNull RecoveryInformation other) {
            return previousConsecutiveNightShifts == other.previousConsecutiveNightShifts && recoveryDaysFollowingNights == other.recoveryDaysFollowingNights;
        }
    }

    static final class LiveComplianceConfig extends LiveConfig<Configuration> {

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
                keyCheckConsecutiveWeekends,
                keyCheckConsecutiveDays,
                keyCheckConsecutiveNightsWorked,
                keyCheckRecoveryDaysFollowingNights,
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
                defaultCheckConsecutiveWeekends,
                defaultCheckConsecutiveDays,
                defaultCheckConsecutiveNightsWorked,
                defaultCheckRecoveryDaysFollowingNights,
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
            keyCheckConsecutiveWeekends = resources.getString(R.string.key_check_consecutive_weekends);
            keyCheckConsecutiveDays = resources.getString(R.string.key_check_consecutive_days);
            keyCheckConsecutiveNightsWorked = resources.getString(R.string.key_check_consecutive_nights_worked);
            keyCheckRecoveryDaysFollowingNights = resources.getString(R.string.key_check_recovery_days_following_nights);
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
            defaultCheckConsecutiveWeekends = resources.getBoolean(R.bool.default_check_consecutive_weekends);
            defaultCheckConsecutiveDays = resources.getBoolean(R.bool.default_check_consecutive_days);
            defaultCheckConsecutiveNightsWorked = resources.getBoolean(R.bool.default_check_consecutive_nights_worked);
            defaultCheckRecoveryDaysFollowingNights = resources.getBoolean(R.bool.default_check_recovery_days_following_nights);
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
                    keyCheckConsecutiveWeekends,
                    keyCheckConsecutiveDays,
                    keyCheckConsecutiveNightsWorked,
                    keyCheckRecoveryDaysFollowingNights,
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
            return new Configuration(
                    sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
                    sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
                    sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
                    sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
                    sharedPreferences.getBoolean(keyCheckLongDaysPerWeek, defaultCheckLongDaysPerWeek),
                    sharedPreferences.getBoolean(keyCheckConsecutiveDays, defaultCheckConsecutiveDays),
                    sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends),
                    sharedPreferences.getBoolean(keyCheckConsecutiveNightsWorked, defaultCheckConsecutiveNightsWorked),
                    sharedPreferences.getBoolean(keyCheckRecoveryDaysFollowingNights, defaultCheckRecoveryDaysFollowingNights),
                    sharedPreferences.getBoolean(keySaferRosters, defaultSaferRosters) ?
                            new Configuration.SaferRostersOptions(
                                    sharedPreferences.getBoolean(keyAllow5ConsecutiveNights, defaultAllow5ConsecutiveNights),
                                    sharedPreferences.getBoolean(keyAllowOnly1RecoveryDayFollowing3Nights, defaultAllowOnly1RecoveryDayFollowing3Nights),
                                    sharedPreferences.getBoolean(keyAllowMidweekRDOs, defaultAllowMidweekRDOs),
                                    sharedPreferences.getBoolean(keyCheckRDOs, defaultCheckRDOs)
                            ) :
                            null
            );
        }

    }
}
