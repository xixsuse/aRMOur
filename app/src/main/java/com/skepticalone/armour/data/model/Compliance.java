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
    public static final int DEFAULT_MAXIMUM_CONSECUTIVE_NIGHTS_WORKED = 7;

    @NonNull
    private final Duration durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    private final Duration durationBetweenShifts;
    @Nullable
    private final LocalDate currentWeekend, lastWeekendWorked;
    @Nullable
    private final Long indexOfNightShift, recoveryDaysFollowingNights;
    @Nullable
    private final Boolean
            compliesWithMaximumDurationOverDay,
            compliesWithMaximumDurationOverWeek,
            compliesWithMaximumDurationOverFortnight,
            sufficientDurationBetweenShifts,
            previousWeekendFree,
            compliesWithMaximumConsecutiveNightsWorked,
            adequateRecovery;
    private final boolean compliant;

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
        durationBetweenShifts = getDurationBetweenShifts(shiftData, previousShifts);
        sufficientDurationBetweenShifts = (configuration.checkDurationBetweenShifts && durationBetweenShifts != null) ? durationBetweenShifts.compareTo(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)) != -1 : null;
        ZonedDateTime weekendStart = shiftData.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        currentWeekend = weekendStart.isBefore(shiftData.getEnd()) && shiftData.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
        lastWeekendWorked = getLastWeekendWorked(currentWeekend, previousShifts);
        previousWeekendFree = (configuration.checkPreviousWeekendFree && currentWeekend != null && lastWeekendWorked != null) ? !currentWeekend.minusWeeks(1).isEqual(lastWeekendWorked) : null;
        RosteredShift previousShift = previousShifts.isEmpty() ? null : previousShifts.get(previousShifts.size() - 1);
        indexOfNightShift = getIndexOfNightShift(shiftData, previousShift);
        compliesWithMaximumConsecutiveNightsWorked = (configuration.maximumConsecutiveNightsWorked != null && indexOfNightShift != null) ? indexOfNightShift < configuration.maximumConsecutiveNightsWorked : null;
        if (indexOfNightShift == null && previousShift != null && previousShift.getCompliance().indexOfNightShift != null) {
            recoveryDaysFollowingNights = shiftData.getStart().toLocalDate().toEpochDay() - previousShift.getShiftData().getEnd().toLocalDate().toEpochDay();
            adequateRecovery = configuration.checkAdequateRecovery != null && previousShift.getCompliance().indexOfNightShift + 1 >= configuration.checkAdequateRecovery.minimumNightsRequiredForRecoveryDays ? recoveryDaysFollowingNights >= configuration.checkAdequateRecovery.minimumRecoveryDays : null;
        } else {
            recoveryDaysFollowingNights = null;
            adequateRecovery = null;
        }
        compliant = (compliesWithMaximumDurationOverDay == null || compliesWithMaximumDurationOverDay) &&
                (compliesWithMaximumDurationOverWeek == null || compliesWithMaximumDurationOverWeek) &&
                (compliesWithMaximumDurationOverFortnight == null || compliesWithMaximumDurationOverFortnight) &&
                (sufficientDurationBetweenShifts == null || sufficientDurationBetweenShifts) &&
                (previousWeekendFree == null || previousWeekendFree) &&
                (compliesWithMaximumConsecutiveNightsWorked == null || compliesWithMaximumConsecutiveNightsWorked) &&
                (adequateRecovery == null || adequateRecovery);
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
    private static Duration getDurationBetweenShifts(@NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        return previousShifts.isEmpty() ? null : Duration.between(previousShifts.get(previousShifts.size() - 1).getShiftData().getEnd(), shiftData.getStart());
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
    public Long getRecoveryDaysFollowingNights() {
        return recoveryDaysFollowingNights;
    }

    @Nullable
    public Boolean adequateRecovery() {
        return adequateRecovery;
    }

    public boolean isCompliant() {
        return compliant;
    }

    static final class Configuration {

        final boolean
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkPreviousWeekendFree;
        @Nullable
        final Integer maximumConsecutiveNightsWorked;
        @Nullable
        final AdequateRecoveryDefinition
                checkAdequateRecovery;

        Configuration(
                boolean checkDurationOverDay,
                boolean checkDurationOverWeek,
                boolean checkDurationOverFortnight,
                boolean checkDurationBetweenShifts,
                boolean checkPreviousWeekendFree,
                @Nullable Integer maximumConsecutiveNightsWorked,
                @Nullable AdequateRecoveryDefinition checkAdequateRecovery
        ) {
            this.checkDurationOverDay = checkDurationOverDay;
            this.checkDurationOverWeek = checkDurationOverWeek;
            this.checkDurationOverFortnight = checkDurationOverFortnight;
            this.checkDurationBetweenShifts = checkDurationBetweenShifts;
            this.checkPreviousWeekendFree = checkPreviousWeekendFree;
            this.maximumConsecutiveNightsWorked = maximumConsecutiveNightsWorked;
            this.checkAdequateRecovery = checkAdequateRecovery;
        }

        @NonNull
        Configuration withCheckDurationOverDay(boolean checkDurationOverDay) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withCheckDurationOverWeek(boolean checkDurationOverWeek) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withCheckDurationOverFortnight(boolean checkDurationOverFortnight) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withCheckDurationBetweenShifts(boolean checkDurationBetweenShifts) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withCheckPreviousWeekendFree(boolean checkPreviousWeekendFree) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withMaximumConsecutiveNightsWorked(@Nullable Integer maximumConsecutiveNightsWorked) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        @NonNull
        Configuration withCheckAdequateRecovery(@Nullable AdequateRecoveryDefinition checkAdequateRecovery) {
            return new Configuration(
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkPreviousWeekendFree,
                    maximumConsecutiveNightsWorked,
                    checkAdequateRecovery
            );
        }

        static final class AdequateRecoveryDefinition {

            // TODO: 21/10/17 remove this
            @NonNull
            static final AdequateRecoveryDefinition DEFAULT = new AdequateRecoveryDefinition(2, 5);

            final int minimumRecoveryDays, minimumNightsRequiredForRecoveryDays;

            AdequateRecoveryDefinition(int minimumRecoveryDays, int minimumNightsRequiredForRecoveryDays) {
                this.minimumRecoveryDays = minimumRecoveryDays;
                this.minimumNightsRequiredForRecoveryDays = minimumNightsRequiredForRecoveryDays;
            }
        }

    }

    static final class LiveComplianceConfig extends LiveConfig<Configuration> {

        @Nullable
        private static LiveComplianceConfig INSTANCE;

        @NonNull
        private final String
                keyCheckDurationOverDay,
                keyCheckDurationOverWeek,
                keyCheckDurationOverFortnight,
                keyCheckDurationBetweenShifts,
                keyCheckConsecutiveWeekends;

        private final boolean
                defaultCheckDurationOverDay,
                defaultCheckDurationOverWeek,
                defaultCheckDurationOverFortnight,
                defaultCheckDurationBetweenShifts,
                defaultCheckConsecutiveWeekends;

        @NonNull
        private final String[] watchKeys;

        private LiveComplianceConfig(@NonNull Resources resources) {
            keyCheckDurationOverDay = resources.getString(R.string.key_check_duration_over_day);
            keyCheckDurationOverWeek = resources.getString(R.string.key_check_duration_over_week);
            keyCheckDurationOverFortnight = resources.getString(R.string.key_check_duration_over_fortnight);
            keyCheckDurationBetweenShifts = resources.getString(R.string.key_check_duration_between_shifts);
            keyCheckConsecutiveWeekends = resources.getString(R.string.key_check_consecutive_weekends);
            defaultCheckDurationOverDay = resources.getBoolean(R.bool.default_check_duration_over_day);
            defaultCheckDurationOverWeek = resources.getBoolean(R.bool.default_check_duration_over_week);
            defaultCheckDurationOverFortnight = resources.getBoolean(R.bool.default_check_duration_over_fortnight);
            defaultCheckDurationBetweenShifts = resources.getBoolean(R.bool.default_check_duration_between_shifts);
            defaultCheckConsecutiveWeekends = resources.getBoolean(R.bool.default_check_consecutive_weekends);
            watchKeys = new String[]{
                    keyCheckDurationOverDay,
                    keyCheckDurationOverWeek,
                    keyCheckDurationOverFortnight,
                    keyCheckDurationBetweenShifts,
                    keyCheckConsecutiveWeekends
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
                    sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends),
                    // TODO: 20/10/17 remove these
                    DEFAULT_MAXIMUM_CONSECUTIVE_NIGHTS_WORKED,
                    Configuration.AdequateRecoveryDefinition.DEFAULT
            );
        }

    }
}
