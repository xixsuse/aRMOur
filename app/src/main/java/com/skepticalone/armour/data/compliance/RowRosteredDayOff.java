package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

final class RowRosteredDayOff extends Row {

    @Nullable
    private final LocalDate rosteredDayOff;

    RowRosteredDayOff(@NonNull ConfigurationSaferRosters configuration, @Nullable LocalDate rosteredDayOff) {
        super(configuration.checkRDOs());
        this.rosteredDayOff = rosteredDayOff;
    }

//    @Nullable
//    public LocalDate getRosteredDayOff() {
//        return rosteredDayOff;
//    }

    @Override
    public boolean isCompliantIfChecked() {
        return rosteredDayOff != null;
    }
}
