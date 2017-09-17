package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.armour.util.ShiftType;

public interface ShiftViewModelContract<Entity> extends DateViewModelContract<Entity> {
    void addNewShift(@NonNull ShiftType shiftType);
}
