package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.compliance.Configuration;

import org.threeten.bp.ZoneId;

import java.util.List;

public final class RosteredShift extends Shift {

    @Nullable
    private final Data loggedShiftData;

    @NonNull
    private final Compliance compliance;

    RosteredShift(
            @NonNull RosteredShiftEntity rawShift,
            @NonNull ZoneId zoneId,
            @NonNull ShiftType.Configuration shiftConfig,
            @NonNull List<RosteredShift> previousShifts,
            @NonNull Configuration complianceConfig
    ) {
        super(rawShift.getId(), rawShift.getComment(), rawShift.getShiftData(), zoneId, shiftConfig);
        loggedShiftData = rawShift.getLoggedShiftData() == null ? null : new Data(rawShift.getLoggedShiftData(), zoneId);
        compliance = new Compliance(getShiftData(), previousShifts, complianceConfig);
    }

    @Nullable
    public Data getLoggedShiftData() {
        return loggedShiftData;
    }

    @NonNull
    public Compliance getCompliance() {
        return compliance;
    }

}
