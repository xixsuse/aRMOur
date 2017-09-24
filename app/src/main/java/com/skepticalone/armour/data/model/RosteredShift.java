package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;

public interface RosteredShift extends Shift {

    @Nullable
    ShiftData getLoggedShiftData();

    @NonNull
    Compliance getCompliance();

    interface Compliance {
        boolean isCompliant();
        @NonNull
        Duration getDurationOverDay();
        boolean exceedsMaximumDurationOverDay();
        @NonNull
        Duration getDurationOverWeek();
        boolean exceedsMaximumDurationOverWeek();
        @NonNull
        Duration getDurationOverFortnight();
        boolean exceedsMaximumDurationOverFortnight();
        @Nullable
        Duration getDurationBetweenShifts();
        boolean insufficientDurationBetweenShifts();
        @Nullable
        LocalDate getCurrentWeekend();
        @Nullable
        LocalDate getLastWeekendWorked();
        boolean consecutiveWeekendsWorked();
    }
}
