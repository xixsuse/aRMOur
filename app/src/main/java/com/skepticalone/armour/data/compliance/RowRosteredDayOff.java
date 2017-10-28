package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

final class RowRosteredDayOff extends Row {

    @Nullable
    private final LocalDate date;

    RowRosteredDayOff(@NonNull ConfigurationSaferRosters configuration, @Nullable LocalDate date) {
        super(configuration.checkRDOs());
        this.date = date;
    }

    @Nullable
    LocalDate getDate() {
        return date;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return date != null;
    }
}
