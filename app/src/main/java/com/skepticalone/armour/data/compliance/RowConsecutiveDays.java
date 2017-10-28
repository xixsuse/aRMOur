package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

class RowConsecutiveDays extends Row {

    private final int indexOfDayShift;

    RowConsecutiveDays(@NonNull Configuration configuration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkConsecutiveDays());
        indexOfDayShift = calculateIndexOfDayShift(dayShift, previousShifts);
    }

    private static int calculateIndexOfDayShift(@NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowConsecutiveDays previousShiftConsecutiveDays = previousShift.getCompliance().getConsecutiveDays();
            if (previousShiftConsecutiveDays != null) {
                LocalDate thisDayShiftDate = dayShift.getStart().toLocalDate(), previousDayShiftDate = previousShift.getShiftData().getStart().toLocalDate();
                if (thisDayShiftDate.equals(previousDayShiftDate)) {
                    return previousShiftConsecutiveDays.indexOfDayShift;
                } else if (previousDayShiftDate.plusDays(1).isEqual(thisDayShiftDate)) {
                    return previousShiftConsecutiveDays.indexOfDayShift + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return indexOfDayShift < getMaximumConsecutiveDays();
    }

    int getIndexOfDayShift() {
        return indexOfDayShift;
    }

    int getMaximumConsecutiveDays() {
        return AppConstants.MAXIMUM_CONSECUTIVE_DAYS;
    }
}
