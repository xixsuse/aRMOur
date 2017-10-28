package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

final class RowConsecutiveNightsSaferRosters extends RowConsecutiveNights {
    private final boolean allow5ConsecutiveNights;

    RowConsecutiveNightsSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, nightShift, previousShifts);
        allow5ConsecutiveNights = configuration.allow5ConsecutiveNights();
    }

    @Override
    int getMaximumConsecutiveNights() {
        return allow5ConsecutiveNights ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT;
    }
}
