package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftDateDialogFragment extends ShiftDateDialogFragment<RosteredShiftEntity> {
    @NonNull
    @Override
    DateViewModelContract<RosteredShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

}
