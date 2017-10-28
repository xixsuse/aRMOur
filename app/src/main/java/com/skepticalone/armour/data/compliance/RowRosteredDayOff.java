package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.Comparators;

import org.threeten.bp.LocalDate;

public final class RowRosteredDayOff extends Row {

    @Nullable
    private final LocalDate date;
    private final boolean allowMidweekRDOs;

    RowRosteredDayOff(@NonNull ConfigurationSaferRosters configuration, @Nullable LocalDate date) {
        super(configuration.checkRDOs());
        this.date = date;
        this.allowMidweekRDOs = configuration.allowMidweekRDOs();
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return date != null;
    }

    public boolean allowMidweekRDOs() {
        return allowMidweekRDOs;
    }

    public final boolean isEqual(@NonNull RowRosteredDayOff other) {
        return
                Comparators.equalDates(date, other.date) &&
                        allowMidweekRDOs == other.allowMidweekRDOs &&
                        equalCompliance(other);
    }
}
