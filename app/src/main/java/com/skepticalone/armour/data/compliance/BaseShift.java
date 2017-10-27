package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import java.util.List;

public abstract class BaseShift {

    @NonNull
    private final Row durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    private final Row durationBetweenShifts;

    public BaseShift(
            @NonNull Configuration configuration,
            @NonNull Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        durationOverDay = new RowDurationOverDay(configuration.checkDurationOverDay(), shift, previousShifts);
        durationOverWeek = new RowDurationOverWeek(configuration.checkDurationOverWeek(), shift, previousShifts);
        durationOverFortnight = new RowDurationOverFortnight(configuration.checkDurationOverFortnight(), shift, previousShifts);
        if (previousShifts.isEmpty()) {
            durationBetweenShifts = null;
        } else {
            Shift.Data previousShift = previousShifts.get(previousShifts.size() - 1).getShiftData();
            durationBetweenShifts = new RowDurationBetweenShifts(configuration.checkDurationBetweenShifts(), shift, previousShift);
        }
    }

    @NonNull
    public Row getDurationOverDay() {
        return durationOverDay;
    }

    @NonNull
    public Row getDurationOverWeek() {
        return durationOverWeek;
    }

    @NonNull
    public Row getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    public Row getDurationBetweenShifts() {
        return durationBetweenShifts;
    }
}
