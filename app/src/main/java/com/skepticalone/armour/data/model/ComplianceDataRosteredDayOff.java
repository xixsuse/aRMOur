package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

public final class ComplianceDataRosteredDayOff extends ComplianceData {

    @Nullable
    private final LocalDate date;
    private final boolean allowMidweekRDOs;

    ComplianceDataRosteredDayOff(@NonNull ConfigurationSaferRosters configuration, @Nullable LocalDate date) {
        super(configuration.checkRDOs());
        this.date = date;
        this.allowMidweekRDOs = configuration.allowMidweekRDOs();
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }

    public boolean allowMidweekRDOs() {
        return allowMidweekRDOs;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return date != null;
    }

}
