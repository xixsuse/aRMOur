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

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class RosteredShift extends Shift {

    @Nullable
    private final Data loggedShiftData;

    @NonNull
    private final Compliance compliance;

    RosteredShift(
            @NonNull RosteredShiftEntity rawShift,
            @NonNull ZoneId zoneId,
            @NonNull ShiftType.Configuration shiftConfig,
            @NonNull List<RosteredShift> previousShifts,
            @NonNull Compliance.Configuration complianceConfig
    ) {
        super(rawShift.getId(), rawShift.getComment(), rawShift.getShiftData(), zoneId, shiftConfig);
        loggedShiftData = rawShift.getLoggedShiftData() == null ? null : new Data(rawShift.getLoggedShiftData(), zoneId);
        LocalDate currentWeekend = getShiftData().calculateCurrentWeekend();
        compliance = new Compliance(
                getDurationSince(getShiftData().getEnd().minusDays(1), previousShifts),
                getDurationSince(getShiftData().getEnd().minusWeeks(1), previousShifts),
                getDurationSince(getShiftData().getEnd().minusWeeks(2), previousShifts),
                getDurationBetweenShifts(previousShifts),
                currentWeekend,
                getLastWeekendWorked(currentWeekend, previousShifts),
                complianceConfig
        );
    }

    @NonNull
    private static Duration getDurationAfterCutoff(@NonNull Data shiftData, @NonNull ZonedDateTime cutOff) {
        return shiftData.getStart().isBefore(cutOff) ? Duration.between(cutOff, shiftData.getEnd()) : shiftData.getDuration();
    }

    @Nullable
    public Data getLoggedShiftData() {
        return loggedShiftData;
    }

    @NonNull
    private Duration getDurationSince(@NonNull ZonedDateTime cutOff, @NonNull List<RosteredShift> previousShifts) {
        Duration totalDuration = getDurationAfterCutoff(getShiftData(), cutOff);
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            Data pastShiftData = previousShifts.get(i).getShiftData();
            if (!pastShiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(getDurationAfterCutoff(pastShiftData, cutOff));
        }
        return totalDuration;
    }

    @Nullable
    private LocalDate getLastWeekendWorked(@Nullable LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
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
    private Duration getDurationBetweenShifts(@NonNull List<RosteredShift> previousShifts){
        return previousShifts.isEmpty() ? null : Duration.between(previousShifts.get(previousShifts.size() - 1).getShiftData().getEnd(), getShiftData().getStart());
    }

    @NonNull
    public Compliance getCompliance() {
        return compliance;
    }

    public static final class Compliance {

        @NonNull
        private final Duration durationOverDay, durationOverWeek, durationOverFortnight;
        @Nullable
        private final Duration durationBetweenShifts;
        @Nullable
        private final LocalDate currentWeekend, lastWeekendWorked;
        private final boolean
                exceedsMaximumDurationOverDay,
                exceedsMaximumDurationOverWeek,
                exceedsMaximumDurationOverFortnight,
                insufficientDurationBetweenShifts,
                consecutiveWeekendsWorked,
                compliant;

        private Compliance(
                @NonNull Duration durationOverDay,
                @NonNull Duration durationOverWeek,
                @NonNull Duration durationOverFortnight,
                @Nullable Duration durationBetweenShifts,
                @Nullable LocalDate currentWeekend,
                @Nullable LocalDate lastWeekendWorked,
                @NonNull Configuration configuration
        ) {
            this.durationOverDay = durationOverDay;
            exceedsMaximumDurationOverDay = configuration.checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(this.durationOverDay);
            this.durationOverWeek = durationOverWeek;
            exceedsMaximumDurationOverWeek = configuration.checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(this.durationOverWeek);
            this.durationOverFortnight = durationOverFortnight;
            exceedsMaximumDurationOverFortnight = configuration.checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(this.durationOverFortnight);
            this.durationBetweenShifts = durationBetweenShifts;
            insufficientDurationBetweenShifts = configuration.checkDurationBetweenShifts && this.durationBetweenShifts != null && AppConstants.insufficientDurationBetweenShifts(this.durationBetweenShifts);
            this.currentWeekend = currentWeekend;
            this.lastWeekendWorked = lastWeekendWorked;
            consecutiveWeekendsWorked = configuration.checkConsecutiveWeekends && this.currentWeekend != null && this.lastWeekendWorked != null && this.currentWeekend.minusWeeks(1).isEqual(this.lastWeekendWorked);
            compliant =
                    !exceedsMaximumDurationOverDay &&
                    !exceedsMaximumDurationOverWeek &&
                    !exceedsMaximumDurationOverFortnight &&
                    !insufficientDurationBetweenShifts &&
                    !consecutiveWeekendsWorked;
        }

        @DrawableRes
        public static int getComplianceIcon(boolean compliant) {
            return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
        }

        @NonNull
        public Duration getDurationOverDay() {
            return durationOverDay;
        }

        public boolean exceedsMaximumDurationOverDay() {
            return exceedsMaximumDurationOverDay;
        }

        @NonNull
        public Duration getDurationOverWeek() {
            return durationOverWeek;
        }

        public boolean exceedsMaximumDurationOverWeek() {
            return exceedsMaximumDurationOverWeek;
        }

        @NonNull
        public Duration getDurationOverFortnight() {
            return durationOverFortnight;
        }

        public boolean exceedsMaximumDurationOverFortnight() {
            return exceedsMaximumDurationOverFortnight;
        }

        @Nullable
        public Duration getDurationBetweenShifts() {
            return durationBetweenShifts;
        }

        public boolean insufficientDurationBetweenShifts() {
            return insufficientDurationBetweenShifts;
        }

        @Nullable
        public LocalDate getCurrentWeekend() {
            return currentWeekend;
        }

        @Nullable
        public LocalDate getLastWeekendWorked() {
            return lastWeekendWorked;
        }

        public boolean consecutiveWeekendsWorked() {
            return consecutiveWeekendsWorked;
        }

        public boolean isCompliant() {
            return compliant;
        }

        static final class Configuration {

            private final boolean
                    checkDurationOverDay,
                    checkDurationOverWeek,
                    checkDurationOverFortnight,
                    checkDurationBetweenShifts,
                    checkConsecutiveWeekends;

            Configuration(
                    boolean checkDurationOverDay,
                    boolean checkDurationOverWeek,
                    boolean checkDurationOverFortnight,
                    boolean checkDurationBetweenShifts,
                    boolean checkConsecutiveWeekends
            ) {
                this.checkDurationOverDay = checkDurationOverDay;
                this.checkDurationOverWeek = checkDurationOverWeek;
                this.checkDurationOverFortnight = checkDurationOverFortnight;
                this.checkDurationBetweenShifts = checkDurationBetweenShifts;
                this.checkConsecutiveWeekends = checkConsecutiveWeekends;
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
                        sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends)
                );
            }

        }
    }
}
