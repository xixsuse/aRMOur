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
    private final RowRecoveryFollowingNights recoveryFollowingNights;

    ComplianceLegacy(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, shift, previousShifts);
        weekend = RowWeekendLegacy.from(configuration, shift, previousShifts);
        if (getNight() == null) {
            recoveryFollowingNights = RowRecoveryFollowingNights.from(configuration, shift, previousShifts);
        } else {
            recoveryFollowingNights = null;
        }
        if (isCompliant()) {
            updateCompliant(
                    (weekend == null || weekend.isCompliant()) &&
                            (recoveryFollowingNights == null || recoveryFollowingNights.isCompliant())
            );
        }

    }

    @Nullable
    @Override
    RowWeekendLegacy getWeekend() {
        return weekend;
    }

    @Override
    @Nullable
    RowRecoveryFollowingNights getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

}
