package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

abstract class RowDurationOverPeriod extends Row {

    @NonNull
    private final Duration duration;

    RowDurationOverPeriod(boolean isChecked, @NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff, @NonNull List<RosteredShift> previousShifts) {
        super(isChecked);
        Duration totalDuration = getDurationAfterCutoff(shiftData, cutOff);
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            Shift.Data pastShiftData = previousShifts.get(i).getShiftData();
            if (!pastShiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(getDurationAfterCutoff(pastShiftData, cutOff));
        }
        duration = totalDuration;
    }

    @NonNull
    private static Duration getDurationAfterCutoff(@NonNull Shift.Data shiftData, @NonNull ZonedDateTime cutOff) {
        return shiftData.getStart().isBefore(cutOff) ? Duration.between(cutOff, shiftData.getEnd()) : shiftData.getDuration();
    }

    @NonNull
    public final Duration getDuration() {
        return duration;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return duration.compareTo(Duration.ofHours(getMaximumHoursOverPeriod())) != 1;
    }

    abstract int getMaximumHoursOverPeriod();
}
