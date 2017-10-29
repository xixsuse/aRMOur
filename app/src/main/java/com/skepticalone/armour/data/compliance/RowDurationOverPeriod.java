package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

abstract class RowDurationOverPeriod extends RowDuration {

    RowDurationOverPeriod(boolean isChecked, @NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff, @NonNull List<RosteredShift> previousShifts) {
        super(isChecked, calculateDurationOverPeriod(shiftData, cutOff, previousShifts));
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

    @Override
    public final boolean isCompliantIfChecked() {
        return getDuration().compareTo(Duration.ofHours(getMaximumHoursOverPeriod())) != 1;
    }

    abstract int getMaximumHoursOverPeriod();

}
