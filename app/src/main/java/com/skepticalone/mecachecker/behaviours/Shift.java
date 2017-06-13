package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.Interval;

public interface Shift extends WithDate {

    int getRowNumberStart();

    @NonNull
    String getColumnNameEnd();

    int getColumnIndexEnd();

    int getRowNumberEnd();

    int getRowNumberShiftType();

    ShiftType getShiftType(Interval shift);
}
