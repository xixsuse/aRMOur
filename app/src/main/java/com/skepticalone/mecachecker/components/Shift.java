package com.skepticalone.mecachecker.components;

import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.Interval;

interface Shift extends Starts, Ends {
    int getRowNumberDate();

    int getRowNumberShiftType();

    ShiftType getShiftType(Interval shift);
}
