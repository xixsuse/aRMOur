package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import java.util.List;

final class ComplianceSaferRosters extends Compliance {

    @Nullable
    private final RowWeekendSaferRosters weekend;
    @Nullable
    private final RowConsecutiveDaysSaferRosters consecutiveDays;
    @Nullable
    private final RowConsecutiveNightsSaferRosters consecutiveNights;
    @Nullable
    private final RowRecoveryFollowingNightsSaferRosters recoveryFollowingNights;

    @Nullable
    private RowRosteredDayOff rosteredDayOff;

    ComplianceSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, shift, previousShifts);
        weekend = RowWeekendSaferRosters.from(configuration, shift, previousShifts);
        if (getShiftType() == ShiftType.NIGHT_SHIFT) {
            consecutiveDays = null;
            recoveryFollowingNights = null;
            consecutiveNights = new RowConsecutiveNightsSaferRosters(configuration, shift, previousShifts);
        } else {
            consecutiveDays = new RowConsecutiveDaysSaferRosters(configuration, shift, previousShifts);
            recoveryFollowingNights = RowRecoveryFollowingNightsSaferRosters.from(configuration, shift, previousShifts);
            consecutiveNights = null;
        }
    }

    @Nullable
    @Override
    RowWeekendSaferRosters getWeekend() {
        return weekend;
    }

    @Nullable
    @Override
    RowConsecutiveDaysSaferRosters getConsecutiveDays() {
        return consecutiveDays;
    }

    @Nullable
    @Override
    RowRecoveryFollowingNightsSaferRosters getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

    @Nullable
    @Override
    RowConsecutiveNightsSaferRosters getConsecutiveNights() {
        return consecutiveNights;
    }

    @Nullable
    RowRosteredDayOff getRosteredDayOff() {
        return rosteredDayOff;
    }

    void setRosteredDayOff(@Nullable RowRosteredDayOff rosteredDayOff) {
        this.rosteredDayOff = rosteredDayOff;
    }
}
