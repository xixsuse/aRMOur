package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import java.util.List;

final class ComplianceLegacy extends Compliance {

    @Nullable
    private final RowWeekendLegacy weekend;
    @Nullable
    private final RowConsecutiveDays consecutiveDays;
    @Nullable
    private final RowConsecutiveNights consecutiveNights;
    @Nullable
    private final RowRecoveryFollowingNights recoveryFollowingNights;

    ComplianceLegacy(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, shift, previousShifts);
        weekend = RowWeekendLegacy.from(configuration, shift, previousShifts);
        if (getShiftType() == ShiftType.NIGHT_SHIFT) {
            consecutiveDays = null;
            recoveryFollowingNights = null;
            consecutiveNights = new RowConsecutiveNights(configuration, shift, previousShifts);
        } else {
            consecutiveDays = new RowConsecutiveDays(configuration, shift, previousShifts);
            recoveryFollowingNights = RowRecoveryFollowingNights.from(configuration, shift, previousShifts);
            consecutiveNights = null;
        }
    }

    @Nullable
    @Override
    RowWeekendLegacy getWeekend() {
        return weekend;
    }

    @Nullable
    @Override
    RowConsecutiveDays getConsecutiveDays() {
        return consecutiveDays;
    }

    @Override
    @Nullable
    RowRecoveryFollowingNights getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

    @Nullable
    @Override
    RowConsecutiveNights getConsecutiveNights() {
        return consecutiveNights;
    }
}
