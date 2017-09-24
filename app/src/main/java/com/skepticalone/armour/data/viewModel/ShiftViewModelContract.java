package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Shift;

public interface ShiftViewModelContract<FinalItem> extends DateViewModelContract<FinalItem> {
    void addNewShift(@NonNull Shift.ShiftType shiftType);
}
