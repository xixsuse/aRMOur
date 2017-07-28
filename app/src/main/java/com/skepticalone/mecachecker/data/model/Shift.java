package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

public interface Shift extends Item {

    @NonNull
    ShiftData getShiftData();

}
