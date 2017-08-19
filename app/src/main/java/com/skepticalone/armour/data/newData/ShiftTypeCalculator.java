package com.skepticalone.armour.data.newData;

import com.skepticalone.armour.util.ShiftUtil;

import org.joda.time.Interval;

interface ShiftTypeCalculator {
    ShiftUtil.ShiftType getShiftType(Interval interval);
}
