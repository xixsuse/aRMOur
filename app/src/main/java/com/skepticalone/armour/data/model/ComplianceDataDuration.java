package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class ComplianceDataDuration extends ComplianceData {

    @NonNull
    private final Duration duration;

    private final int hoursOverPeriod;

    private final boolean maximum;

    private ComplianceDataDuration(boolean isChecked, @NonNull Duration duration, int hoursOverPeriod, boolean maximum) {
        super(isChecked);
        this.duration = duration;
        this.hoursOverPeriod = hoursOverPeriod;
        this.maximum = maximum;
    }

    @NonNull
    private static Duration calculateDurationOverPeriod(@NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff, @NonNull List<RosteredShift> previousShifts) {
        Duration totalDuration = calculateDurationAfterCutoff(shiftData, cutOff);
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            Shift.Data pastShiftData = previousShifts.get(i).getShiftData();
            if (!pastShiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(calculateDurationAfterCutoff(pastShiftData, cutOff));
        }
        return totalDuration;
    }

    @NonNull
    private static Duration calculateDurationAfterCutoff(@NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff) {
        return shiftData.getStart().isBefore(cutOff) ? Duration.between(cutOff, shiftData.getEnd()) : shiftData.getDuration();
    }

    @NonNull
    static ComplianceDataDuration overDay(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        return new ComplianceDataDuration(configuration.checkDurationOverDay(), calculateDurationOverPeriod(shiftData, shiftData.getEnd().minusDays(1), previousShifts), AppConstants.MAXIMUM_HOURS_OVER_DAY, true);
    }

    @NonNull
    static ComplianceDataDuration overWeek(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        return new ComplianceDataDuration(configuration.checkDurationOverDay(), calculateDurationOverPeriod(shiftData, shiftData.getEnd().minusWeeks(1), previousShifts), AppConstants.MAXIMUM_HOURS_OVER_WEEK, true);
    }

    @NonNull
    static ComplianceDataDuration overFortnight(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        return new ComplianceDataDuration(configuration.checkDurationOverDay(), calculateDurationOverPeriod(shiftData, shiftData.getEnd().minusWeeks(2), previousShifts), AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT, true);
    }

    @Nullable
    static ComplianceDataDuration betweenShifts(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        return previousShifts.isEmpty() ? null : new ComplianceDataDuration(configuration.checkDurationBetweenShifts(), Duration.between(previousShifts.get(previousShifts.size() - 1).getShiftData().getEnd(), shift.getStart()), AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS, false);
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return duration.compareTo(Duration.ofHours(hoursOverPeriod)) != (maximum ? 1 : -1);
    }

    @NonNull
    public Duration getDuration() {
        return duration;
    }

}
