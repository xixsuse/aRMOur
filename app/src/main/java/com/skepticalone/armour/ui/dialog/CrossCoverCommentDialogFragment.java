package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class CrossCoverCommentDialogFragment extends CommentDialogFragment<CrossCover> {

    private ItemViewModelContract<CrossCover> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    ItemViewModelContract<CrossCover> getViewModel() {
        return viewModel;
    }

}
