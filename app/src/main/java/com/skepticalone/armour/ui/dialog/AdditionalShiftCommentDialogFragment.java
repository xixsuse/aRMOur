package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftCommentDialogFragment extends CommentDialogFragment<AdditionalShift> {

    private ItemViewModelContract<AdditionalShift> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShift> getViewModel() {
        return viewModel;
    }

}