package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.ShiftData;

public interface Shift extends Item {

    @NonNull
    ShiftData getShift();

}