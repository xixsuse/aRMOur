package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;

public final class AdditionalShiftDateDialogFragment extends ShiftDateDialogFragment<AdditionalShift> {

    @NonNull
    @Override
    DateViewModelContract<AdditionalShift> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
