package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

final class RowConsecutiveDaysSaferRosters extends RowConsecutiveDays {
    RowConsecutiveDaysSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, dayShift, previousShifts);
    }

    @Override
    int getMaximumConsecutiveDays() {
        return AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_DAYS;
    }
}
