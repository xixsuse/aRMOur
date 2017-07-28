package com.skepticalone.mecachecker.data.model;

import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;

public interface RosteredShift extends Shift {

    @Nullable
    ShiftData getLoggedShiftData();

}
