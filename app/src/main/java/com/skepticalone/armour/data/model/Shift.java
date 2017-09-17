package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.util.ShiftType;

public interface Shift extends Item {

    @NonNull
    ShiftData getShiftData();

    @NonNull
    ShiftType getShiftType();

}
