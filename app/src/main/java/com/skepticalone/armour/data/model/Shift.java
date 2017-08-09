package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.util.ShiftData;

public interface Shift extends Item {

    @NonNull
    ShiftData getShiftData();

}
