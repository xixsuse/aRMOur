package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;

public final class AdditionalShiftDateDialogFragment extends ShiftDateDialogFragment<AdditionalShiftEntity> {

    @NonNull
    @Override
    DateViewModelContract<AdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
