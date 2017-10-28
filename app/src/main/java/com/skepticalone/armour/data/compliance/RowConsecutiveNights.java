package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

class RowConsecutiveNights extends Row {

    private final int indexOfNightShift;

    RowConsecutiveNights(@NonNull Configuration configuration, @NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkConsecutiveNightsWorked());
        indexOfNightShift = calculateIndexOfNightShift(nightShift, previousShifts);
    }

    private static int calculateIndexOfNightShift(@NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowConsecutiveNights previousShiftNights = previousShift.getCompliance().getConsecutiveNights();
            if (previousShiftNights != null) {
                LocalDate thisNightShiftDate = nightShift.getEnd().toLocalDate(), previousNightShiftDate = previousShift.getShiftData().getEnd().toLocalDate();
                if (thisNightShiftDate.equals(previousNightShiftDate)) {
                    return previousShiftNights.indexOfNightShift;
                } else if (previousNightShiftDate.plusDays(1) == thisNightShiftDate) {
                    return previousShiftNights.indexOfNightShift + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return indexOfNightShift < getMaximumConsecutiveNights();
    }

    final int getIndexOfNightShift() {
        return indexOfNightShift;
    }

    int getMaximumConsecutiveNights() {
        return AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS;
    }
}
