package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface ShiftModel {

    @MainThread
    void addNewShift(@NonNull LocalTime start, @NonNull LocalTime end);

    @MainThread
    void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end);

}