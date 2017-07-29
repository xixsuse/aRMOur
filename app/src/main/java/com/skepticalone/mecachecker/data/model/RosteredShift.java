package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

public interface RosteredShift extends Shift {

    @Nullable
    ShiftData getLoggedShiftData();
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
