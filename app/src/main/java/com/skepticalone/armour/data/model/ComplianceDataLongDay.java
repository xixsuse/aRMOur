package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;

import java.util.List;

public final class ComplianceDataLongDay extends ComplianceDataIndex {

    private ComplianceDataLongDay(boolean isChecked, int indexOfLongDay) {
        super(isChecked, indexOfLongDay);
    }

    @Nullable
    static ComplianceDataLongDay from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        return calculateIsLongDay(shift) ? new ComplianceDataLongDay(configuration.checkLongDaysPerWeek(), calculateIndexOfLongDay(shift, previousShifts)) : null;
    }

    private static boolean calculateIsLongDay(Shift.Data shift) {
        return shift.getDuration().compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY)) == 1;
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
        return getIndex() < AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK;
    }

}
