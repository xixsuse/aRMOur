package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftDateDialogFragment extends ShiftDateDialogFragment<RawRosteredShiftEntity> {
    @NonNull
    @Override
    DateViewModelContract<RawRosteredShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

}
