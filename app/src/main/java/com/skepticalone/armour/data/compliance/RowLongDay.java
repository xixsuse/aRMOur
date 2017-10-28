package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class RowLongDay extends Row {

    private final int indexOfLongDay;

    RowLongDay(@NonNull Configuration configuration, @NonNull Shift.Data longDay, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkLongDaysPerWeek());
        indexOfLongDay = calculateIndexOfLongDay(longDay, previousShifts);
    }

    private static int calculateIndexOfLongDay(@NonNull Shift.Data longDay, @NonNull List<RosteredShift> previousShifts) {
        int index = 0;
        if (!previousShifts.isEmpty()) {
            final LocalDate oneWeekAgo = longDay.getStart().toLocalDate().minusWeeks(1);
            for (int i = previousShifts.size() - 1; i >= 0; i--) {
                RosteredShift previousShift = previousShifts.get(i);
                if (!previousShift.getShiftData().getStart().toLocalDate().isAfter(oneWeekAgo)) {
                    break;
                } else if (previousShift.getCompliance().getLongDay() != null) {
                    index++;
                }
            }
        }
        return index;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return indexOfLongDay < AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK;
    }

    public int getIndexOfLongDay() {
        return indexOfLongDay;
    }

    public final boolean isEqual(@NonNull RowLongDay other) {
        return
                indexOfLongDay == other.indexOfLongDay &&
                        equalCompliance(other);
    }
}
