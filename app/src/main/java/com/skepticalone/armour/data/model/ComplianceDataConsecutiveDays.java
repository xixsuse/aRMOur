package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class ComplianceDataConsecutiveDays extends ComplianceDataIndex {

    private final boolean saferRosters;

    private ComplianceDataConsecutiveDays(boolean isChecked, boolean saferRosters, int indexOfDayShift) {
        super(isChecked, indexOfDayShift);
        this.saferRosters = saferRosters;
    }

    @NonNull
    static ComplianceDataConsecutiveDays from(@NonNull ComplianceConfiguration complianceConfiguration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        return new ComplianceDataConsecutiveDays(
                complianceConfiguration.checkConsecutiveDays(),
                complianceConfiguration instanceof ComplianceConfigurationSaferRosters,
                calculateIndexOfDayShift(dayShift, previousShifts)
        );
    }

    private static int calculateIndexOfDayShift(@NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            ComplianceDataConsecutiveDays previousShiftConsecutiveDays = previousShift.getCompliance().getConsecutiveDays();
            if (previousShiftConsecutiveDays != null) {
                LocalDate thisDayShiftDate = dayShift.getStart().toLocalDate(), previousDayShiftDate = previousShift.getShiftData().getStart().toLocalDate();
                if (thisDayShiftDate.equals(previousDayShiftDate)) {
                    return previousShiftConsecutiveDays.getIndex();
                } else if (previousDayShiftDate.plusDays(1).isEqual(thisDayShiftDate)) {
                    return previousShiftConsecutiveDays.getIndex() + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return getIndex() < getMaximumConsecutiveDays();
    }

    public final boolean isSaferRosters() {
        return saferRosters;
    }

    public final int getMaximumConsecutiveDays() {
        return saferRosters ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_DAYS : AppConstants.MAXIMUM_CONSECUTIVE_DAYS;
    }

}
