package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

public final class RowDurationOverWeek extends RowDurationOverPeriod {
    RowDurationOverWeek(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkDurationOverWeek(), shiftData, shiftData.getEnd().minusWeeks(1), previousShifts);
    }

    @Override
    int getMaximumHoursOverPeriod() {
        return AppConstants.MAXIMUM_HOURS_OVER_WEEK;
    }

}
