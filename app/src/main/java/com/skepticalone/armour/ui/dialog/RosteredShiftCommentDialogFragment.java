package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftCommentDialogFragment extends CommentDialogFragment<RosteredShift> {

    private ItemViewModelContract<RosteredShift> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    ItemViewModelContract<RosteredShift> getViewModel() {
        return viewModel;
    }

}
