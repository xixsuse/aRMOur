package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftUtil;

public interface ShiftItemViewModelContract<Entity> extends DateItemViewModelContract<Entity> {
    void addNewShift(@NonNull ShiftUtil.ShiftType shiftType);
}
