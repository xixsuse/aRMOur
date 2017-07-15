package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftType;

import org.joda.time.LocalTime;

public interface ShiftViewModel<T> extends BaseItemViewModel<T> {

    @MainThread
    void addShift(@NonNull ShiftType shiftType);

    @MainThread
    void setStart(long id, @NonNull LocalTime start);

    @MainThread
    void setEnd(long id, @NonNull LocalTime end);

}