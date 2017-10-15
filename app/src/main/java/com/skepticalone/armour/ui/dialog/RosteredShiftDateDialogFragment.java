package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftDateDialogFragment extends ShiftDateDialogFragment<RosteredShift> {

    private DateViewModelContract<RosteredShift> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    DateViewModelContract<RosteredShift> getViewModel() {
        return viewModel;
    }

}
