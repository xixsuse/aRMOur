package com.skepticalone.armour.data.newData;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;

import java.util.List;


final class LiveComplianceChecker extends LiveData<ComplianceChecker> implements SharedPreferences.OnSharedPreferenceChangeListener {

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

    LiveComplianceChecker(@NonNull Application application) {
        Resources resources = application.getResources();
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
        updateChecker(PreferenceManager.getDefaultSharedPreferences(application));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (
                key.equals(keyCheckDurationOverDay) ||
                        key.equals(keyCheckDurationOverWeek) ||
                        key.equals(keyCheckDurationOverFortnight) ||
                        key.equals(keyCheckDurationBetweenShifts) ||
                        key.equals(keyCheckConsecutiveWeekends)
                ) {
            updateChecker(sharedPreferences);
        }
    }

    private void updateChecker(@NonNull SharedPreferences sharedPreferences) {
        setValue(new CheckerImplementation(
                sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
                sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
                sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
                sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
                sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends)
        ));
    }

    private static final class CheckerImplementation implements ComplianceChecker {

        private final boolean
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkConsecutiveWeekends;

        private CheckerImplementation(
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

        private static Duration getDurationSince(@NonNull List<RosteredShift> shifts, int currentIndex, @NonNull ReadableInstant cutOff) {
            Duration totalDuration = Duration.ZERO;
            do {
                RosteredShift shift = shifts.get(currentIndex);
                if (shift.interval.isBefore(cutOff)) break;
                totalDuration = totalDuration.plus(shift.interval.getStart().isBefore(cutOff) ? new Duration(cutOff, shift.interval.getEnd()) : shift.interval.toDuration());
            } while (--currentIndex >= 0);
            return totalDuration;
        }

        @Override
        public void process(@NonNull List<RosteredShift> shifts) {
            @Nullable LocalDate lastElapsedWeekendWorked = null;
            @Nullable LocalDate lastWeekendProcessed = null;
            for (int currentIndex = 0, count = shifts.size(); currentIndex < count; currentIndex++) {
                RosteredShift shift = shifts.get(currentIndex);
                shift.durationOverDay = getDurationSince(shifts, currentIndex, shift.interval.getEnd().minusDays(1));
                shift.exceedsMaximumDurationOverDay = checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(shift.durationOverDay);
                shift.durationOverWeek = getDurationSince(shifts, currentIndex, shift.interval.getEnd().minusWeeks(1));
                shift.exceedsMaximumDurationOverWeek = checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(shift.durationOverWeek);
                shift.durationOverFortnight = getDurationSince(shifts, currentIndex, shift.interval.getEnd().minusWeeks(2));
                shift.exceedsMaximumDurationOverFortnight = checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(shift.durationOverFortnight);
                shift.durationBetweenShifts = currentIndex == 0 ? null : new Duration(shifts.get(currentIndex - 1).interval.getEnd(), shift.interval.getStart());
                shift.insufficientDurationBetweenShifts = shift.durationBetweenShifts != null && checkDurationBetweenShifts && AppConstants.insufficientDurationBetweenShifts(shift.durationBetweenShifts);
                DateTime weekendStart = shift.interval.getStart().withDayOfWeek(DateTimeConstants.SATURDAY).withTimeAtStartOfDay();
                if (weekendStart.isBefore(shift.interval.getEnd()) && shift.interval.getStart().isBefore(weekendStart.plusDays(2))) {
                    // I am working this weekend
                    shift.currentWeekend = weekendStart.toLocalDate();
                    if (lastWeekendProcessed != null && !shift.currentWeekend.isEqual(lastWeekendProcessed)) {
                        // the last weekend processed was different to this one
                        lastElapsedWeekendWorked = lastWeekendProcessed;
                    }
                    shift.lastWeekendWorked = lastElapsedWeekendWorked;
                    //noinspection ConstantConditions
                    shift.consecutiveWeekendsWorked = checkConsecutiveWeekends && shift.lastWeekendWorked != null && !shift.lastWeekendWorked.isEqual(shift.currentWeekend) && shift.lastWeekendWorked.isEqual(shift.currentWeekend.minusWeeks(1));
                    lastWeekendProcessed = shift.currentWeekend;
                } else {
                    shift.currentWeekend = shift.lastWeekendWorked = null;
                    shift.consecutiveWeekendsWorked = false;
                }
                shift.compliant =
                        !shift.exceedsMaximumDurationOverDay &&
                                !shift.exceedsMaximumDurationOverWeek &&
                                !shift.exceedsMaximumDurationOverFortnight &&
                                !shift.insufficientDurationBetweenShifts &&
                                !shift.consecutiveWeekendsWorked;
            }
        }

    }
}
