package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.armour.util.ShiftUtil;

public interface ShiftViewModelContract<Entity> extends DateViewModelContract<Entity> {
    void addNewShift(@NonNull ShiftUtil.ShiftType shiftType);
}
