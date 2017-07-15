package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.model.AdditionalShift;
import com.skepticalone.mecachecker.util.ShiftType;

import static com.skepticalone.mecachecker.ui.Constants.ITEM_TYPE_ADDITIONAL_SHIFT;

public final class AdditionalShiftListFragment
        extends ShiftAddListFragment<AdditionalShift, AdditionalShiftEntity, AdditionalShiftViewModel> {

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

    @Override
    void addNewShift(@NonNull ShiftType shiftType) {
        getViewModel().addNewShift(shiftType);
    }

    @NonNull
    @Override
    ItemListAdapter<AdditionalShift> createAdapter() {
        return new AdditionalShiftListAdapter(this);
    }

    @Override
    int getItemType() {
        return ITEM_TYPE_ADDITIONAL_SHIFT;
    }

}
