package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftDateDialogFragment extends ShiftDateDialogFragment<RosteredShiftEntity> {

    @NonNull
    @Override
    DateViewModelContract<RosteredShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(RosteredShiftViewModel.class);
    }

}