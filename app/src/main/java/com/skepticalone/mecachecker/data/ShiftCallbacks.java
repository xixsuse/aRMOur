package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface ShiftCallbacks {

    @MainThread
    void addNewShift(@NonNull ShiftType shiftType);

    @MainThread
    void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end);

}