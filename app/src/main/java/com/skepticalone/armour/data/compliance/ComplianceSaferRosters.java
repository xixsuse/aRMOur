package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import java.util.List;

public final class ComplianceSaferRosters extends Compliance {

    @Nullable
    private final RowWeekendSaferRosters weekend;
    @Nullable
    private final RowRecoveryFollowingNightsSaferRosters recoveryFollowingNights;

    @Nullable
    private RowRosteredDayOff rosteredDayOff;

    ComplianceSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, shift, previousShifts);
        weekend = RowWeekendSaferRosters.from(configuration, shift, previousShifts);
        if (getNight() == null) {
            recoveryFollowingNights = RowRecoveryFollowingNightsSaferRosters.from(configuration, shift, previousShifts);
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
    public RowWeekendSaferRosters getWeekend() {
        return weekend;
    }

    @Nullable
    @Override
    public RowRecoveryFollowingNightsSaferRosters getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

    @Nullable
    public RowRosteredDayOff getRosteredDayOff() {
        return rosteredDayOff;
    }

    void setRosteredDayOff(@Nullable RowRosteredDayOff rosteredDayOff) {
        this.rosteredDayOff = rosteredDayOff;
    }
}
