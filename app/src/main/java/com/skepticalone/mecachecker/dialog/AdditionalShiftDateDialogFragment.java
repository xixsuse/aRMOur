package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;

public final class AdditionalShiftDateDialogFragment extends ShiftDateDialogFragment<AdditionalShiftEntity> {

    @NonNull
    @Override
    DateViewModelContract<AdditionalShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

}