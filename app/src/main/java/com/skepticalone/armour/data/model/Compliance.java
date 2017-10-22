package com.skepticalone.armour.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.LiveConfig;

import org.threeten.bp.DayOfWeek;
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
    private final Integer indexOfLongDay;
    @Nullable
    private final LocalDate currentWeekend, lastWeekendWorked;
    @Nullable
    private final Long indexOfNightShift;
    @Nullable
    private final RecoveryInformation recoveryInformation;
    @Nullable
    private final Boolean
            compliesWithMaximumDurationOverDay,
            compliesWithMaximumDurationOverWeek,
            compliesWithMaximumDurationOverFortnight,
            sufficientDurationBetweenShifts,
            compliesWithMaximumLongDaysPerWeek,
            previousWeekendFree,
            compliesWithMaximumConsecutiveNightsWorked,
            sufficientRecoveryDaysFollowingNights;
    private final boolean compliant;

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
        ZonedDateTime weekendStart = shiftData.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        currentWeekend = weekendStart.isBefore(shiftData.getEnd()) && shiftData.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
        lastWeekendWorked = getLastWeekendWorked(currentWeekend, previousShifts);
        previousWeekendFree = configuration.checkConsecutiveWeekends && currentWeekend != null && lastWeekendWorked != null ? !currentWeekend.minusWeeks(1).isEqual(lastWeekendWorked) : null;
        indexOfNightShift = getIndexOfNightShift(shiftData, previousShift);
        compliesWithMaximumConsecutiveNightsWorked = configuration.checkConsecutiveNightsWorked && indexOfNightShift != null ? indexOfNightShift < getMaximumConsecutiveNights(configuration.saferRostersOptions) : null;
        recoveryInformation = indexOfNightShift == null && previousShift != null && previousShift.getCompliance().indexOfNightShift != null ? new RecoveryInformation(previousShift.getCompliance().indexOfNightShift, shiftData.getStart().toLocalDate().toEpochDay() - previousShift.getShiftData().getEnd().toLocalDate().toEpochDay()) : null;
        sufficientRecoveryDaysFollowingNights = configuration.checkRecoveryDaysFollowingNights && recoveryInformation != null ? recoveryInformation.previousConsecutiveNightShifts < getMinimumNightsBeforeRecovery(configuration.saferRostersOptions) || recoveryInformation.recoveryDaysFollowingNights >= getMinimumRecoveryDaysFollowingNights(configuration.saferRostersOptions, recoveryInformation.previousConsecutiveNightShifts) : null;
        indexOfLongDay = indexOfNightShift == null ? getIndexOfLongDay(shiftData, previousShifts) : null;
        compliesWithMaximumLongDaysPerWeek = configuration.checkLongDaysPerWeek && indexOfLongDay != null ? indexOfLongDay < AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK : null;
        compliant = (compliesWithMaximumDurationOverDay == null || compliesWithMaximumDurationOverDay) &&
                (compliesWithMaximumDurationOverWeek == null || compliesWithMaximumDurationOverWeek) &&
                (compliesWithMaximumDurationOverFortnight == null || compliesWithMaximumDurationOverFortnight) &&
                (sufficientDurationBetweenShifts == null || sufficientDurationBetweenShifts) &&
                (compliesWithMaximumLongDaysPerWeek == null || compliesWithMaximumLongDaysPerWeek) &&
                (previousWeekendFree == null || previousWeekendFree) &&
                (compliesWithMaximumConsecutiveNightsWorked == null || compliesWithMaximumConsecutiveNightsWorked) &&
                (sufficientRecoveryDaysFollowingNights == null || sufficientRecoveryDaysFollowingNights);
    }

    public static int getMaximumConsecutiveNights(@Nullable Configuration.SaferRostersOptions saferRostersOptions) {
        return saferRostersOptions == null ? AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS : saferRostersOptions.allow5ConsecutiveNights ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT;
    }

    public static int getMinimumNightsBeforeRecovery(@Nullable Configuration.SaferRostersOptions saferRostersOptions) {
        return saferRostersOptions == null ? AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY : AppConstants.SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY;
    }

    public static int getMinimumRecoveryDaysFollowingNights(@Nullable Configuration.SaferRostersOptions saferRostersOptions, long previousConsecutiveNightShifts) {
        return saferRostersOptions == null ? AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS :
                previousConsecutiveNightShifts < 3 ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS :
                        previousConsecutiveNightShifts > 3 ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS :
                                saferRostersOptions.allowOnly1RecoveryDayFollowing3Nights ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT;
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
    private static LocalDate getLastWeekendWorked(@Nullable LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        if (previousShifts.isEmpty()) return null;
        RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
        LocalDate previousShiftWeekend = previousShift.getCompliance().getCurrentWeekend();
        if (previousShiftWeekend == null || (currentWeekend != null && previousShiftWeekend.isEqual(currentWeekend))) {
            return previousShift.getCompliance().getLastWeekendWorked();
        } else {
            return previousShiftWeekend;
        }
    }

    @Nullable
    private static Long getIndexOfNightShift(@NonNull Shift.Data shiftData, @Nullable RosteredShift previousShift) {
        LocalDate shiftEnd = shiftData.getEnd().toLocalDate();
        if (!shiftData.getStart().toLocalDate().isEqual(shiftEnd) || shiftData.getStart().toLocalTime().isBefore(LocalTime.of(6, 0))) {
            if (previousShift != null && previousShift.getCompliance().indexOfNightShift != null) {
                LocalDate previousShiftEnd = previousShift.getShiftData().getEnd().toLocalDate();
                if (!previousShiftEnd.plusDays(1).isBefore(shiftEnd)) {
                    return previousShift.getCompliance().indexOfNightShift + (shiftEnd.toEpochDay() - previousShiftEnd.toEpochDay());
                }
            }
            return 0L;
        }
        return null;
    }

    @Nullable
    private Integer getIndexOfLongDay(@NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        if (shiftData.getDuration().compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY)) == 1) {
            final LocalDate oneWeekAgo = shiftData.getStart().toLocalDate().minusWeeks(1);
            int index = 0;
            for (int i = previousShifts.size() - 1; i >= 0; i--) {
                RosteredShift previousShift = previousShifts.get(i);
                if (!previousShift.getShiftData().getStart().toLocalDate().isAfter(oneWeekAgo))
                    break;
                else if (previousShift.getCompliance().indexOfLongDay != null) index++;
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
    public Integer getIndexOfLongDay() {
        return indexOfLongDay;
    }

    @Nullable
    public Boolean compliesWithMaximumLongDaysPerWeek() {
        return compliesWithMaximumLongDaysPerWeek;
    }

    @Nullable
    public LocalDate getCurrentWeekend() {
        return currentWeekend;
    }

    @Nullable
    public LocalDate getLastWeekendWorked() {
        return lastWeekendWorked;
    }

    @Nullable
    public Boolean previousWeekendFree() {
        return previousWeekendFree;
    }

    @Nullable
    public Long getIndexOfNightShift() {
        return indexOfNightShift;
    }

    @Nullable
    public Boolean compliesWithMaximumConsecutiveNightsWorked() {
        return compliesWithMaximumConsecutiveNightsWorked;
    }

    @Nullable
    public RecoveryInformation getRecoveryInformation() {
        return recoveryInformation;
    }

    @Nullable
    public Boolean sufficientRecoveryFollowingNights() {
        return sufficientRecoveryDaysFollowingNights;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public static final class RecoveryInformation {
        public final long previousConsecutiveNightShifts, recoveryDaysFollowingNights;

        private RecoveryInformation(long indexOfPreviousNightShift, long recoveryDaysFollowingNights) {
            previousConsecutiveNightShifts = indexOfPreviousNightShift + 1;
            this.recoveryDaysFollowingNights = recoveryDaysFollowingNights;
        }

        public boolean isEqualTo(@NonNull RecoveryInformation other) {
            return previousConsecutiveNightShifts == other.previousConsecutiveNightShifts && recoveryDaysFollowingNights == other.recoveryDaysFollowingNights;
        }
    }

    public static final class Configuration {

        final boolean
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkLongDaysPerWeek,
                checkConsecutiveWeekends,
                checkConsecutiveNightsWorked,
                checkRecoveryDaysFollowingNights;

        @Nullable
        final SaferRostersOptions saferRostersOptions;

        Configuration(
                boolean checkDurationOverDay,
                boolean checkDurationOverWeek,
                boolean checkDurationOverFortnight,
                boolean checkDurationBetweenShifts,
                boolean checkLongDaysPerWeek,
                boolean checkConsecutiveWeekends,
                boolean checkConsecutiveNightsWorked,
                boolean checkRecoveryDaysFollowingNights,
                @Nullable SaferRostersOptions saferRostersOptions
        ) {
            this.checkDurationOverDay = checkDurationOverDay;
            this.checkDurationOverWeek = checkDurationOverWeek;
            this.checkDurationOverFortnight = checkDurationOverFortnight;
            this.checkDurationBetweenShifts = checkDurationBetweenShifts;
            this.checkLongDaysPerWeek = checkLongDaysPerWeek;
            this.checkConsecutiveWeekends = checkConsecutiveWeekends;
            this.checkConsecutiveNightsWorked = checkConsecutiveNightsWorked;
            this.checkRecoveryDaysFollowingNights = checkRecoveryDaysFollowingNights;
            this.saferRostersOptions = saferRostersOptions;
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckDurationOverDay(boolean checkDurationOverDay) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckDurationOverWeek(boolean checkDurationOverWeek) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckDurationOverFortnight(boolean checkDurationOverFortnight) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckDurationBetweenShifts(boolean checkDurationBetweenShifts) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckLongDaysPerWeek(boolean checkLongDaysPerWeek) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckConsecutiveWeekends(boolean checkConsecutiveWeekends) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckConsecutiveNightsWorked(boolean checkConsecutiveNightsWorked) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @SuppressWarnings("SameParameterValue")
        @NonNull
        Configuration withCheckRecoveryDaysFollowingNights(boolean checkRecoveryDaysFollowingNights) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        @NonNull
        Configuration withSaferRostersOptions(@Nullable SaferRostersOptions saferRostersOptions) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkLongDaysPerWeek,
                    checkConsecutiveWeekends,
                    checkConsecutiveNightsWorked,
                    checkRecoveryDaysFollowingNights,
                    saferRostersOptions
            );
        }

        public static final class SaferRostersOptions {
            final boolean allow5ConsecutiveNights, allowOnly1RecoveryDayFollowing3Nights;

            public SaferRostersOptions(boolean allow5ConsecutiveNights, boolean allowOnly1RecoveryDayFollowing3Nights) {
                this.allow5ConsecutiveNights = allow5ConsecutiveNights;
                this.allowOnly1RecoveryDayFollowing3Nights = allowOnly1RecoveryDayFollowing3Nights;
            }

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
                keyCheckConsecutiveNightsWorked,
                keyCheckRecoveryDaysFollowingNights,
                keyAllow5ConsecutiveNights,
                keyAllowOnly1RecoveryDayFollowing3Nights;

        private final boolean
                defaultSaferRosters,
                defaultCheckDurationOverDay,
                defaultCheckDurationOverWeek,
                defaultCheckDurationOverFortnight,
                defaultCheckDurationBetweenShifts,
                defaultCheckLongDaysPerWeek,
                defaultCheckConsecutiveWeekends,
                defaultCheckConsecutiveNightsWorked,
                defaultCheckRecoveryDaysFollowingNights,
                defaultAllow5ConsecutiveNights,
                defaultAllowOnly1RecoveryDayFollowing3Nights;

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
            keyCheckConsecutiveNightsWorked = resources.getString(R.string.key_check_consecutive_nights_worked);
            keyCheckRecoveryDaysFollowingNights = resources.getString(R.string.key_check_recovery_days_following_nights);
            keyAllow5ConsecutiveNights = resources.getString(R.string.key_allow_5_consecutive_nights);
            keyAllowOnly1RecoveryDayFollowing3Nights = resources.getString(R.string.key_allow_only_1_recovery_day_following_3_nights);
            defaultSaferRosters = resources.getBoolean(R.bool.default_safer_rosters);
            defaultCheckDurationOverDay = resources.getBoolean(R.bool.default_check_duration_over_day);
            defaultCheckDurationOverWeek = resources.getBoolean(R.bool.default_check_duration_over_week);
            defaultCheckDurationOverFortnight = resources.getBoolean(R.bool.default_check_duration_over_fortnight);
            defaultCheckDurationBetweenShifts = resources.getBoolean(R.bool.default_check_duration_between_shifts);
            defaultCheckLongDaysPerWeek = resources.getBoolean(R.bool.default_check_long_days_per_week);
            defaultCheckConsecutiveWeekends = resources.getBoolean(R.bool.default_check_consecutive_weekends);
            defaultCheckConsecutiveNightsWorked = resources.getBoolean(R.bool.default_check_consecutive_nights_worked);
            defaultCheckRecoveryDaysFollowingNights = resources.getBoolean(R.bool.default_check_recovery_days_following_nights);
            defaultAllow5ConsecutiveNights = resources.getBoolean(R.bool.default_allow_5_consecutive_nights);
            defaultAllowOnly1RecoveryDayFollowing3Nights = resources.getBoolean(R.bool.default_allow_only_1_recovery_day_following_3_nights);
            watchKeys = new String[]{
                    keySaferRosters,
                    keyCheckDurationOverDay,
                    keyCheckDurationOverWeek,
                    keyCheckDurationOverFortnight,
                    keyCheckDurationBetweenShifts,
                    keyCheckLongDaysPerWeek,
                    keyCheckConsecutiveWeekends,
                    keyCheckConsecutiveNightsWorked,
                    keyCheckRecoveryDaysFollowingNights,
                    keyAllow5ConsecutiveNights,
                    keyAllowOnly1RecoveryDayFollowing3Nights
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
                    sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends),
                    sharedPreferences.getBoolean(keyCheckConsecutiveNightsWorked, defaultCheckConsecutiveNightsWorked),
                    sharedPreferences.getBoolean(keyCheckRecoveryDaysFollowingNights, defaultCheckRecoveryDaysFollowingNights),
                    sharedPreferences.getBoolean(keySaferRosters, defaultSaferRosters) ? new Configuration.SaferRostersOptions(sharedPreferences.getBoolean(keyAllow5ConsecutiveNights, defaultAllow5ConsecutiveNights), sharedPreferences.getBoolean(keyAllowOnly1RecoveryDayFollowing3Nights, defaultAllowOnly1RecoveryDayFollowing3Nights)) : null
            );
        }

    }
}
