package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftCommentDialogFragment extends CommentDialogFragment<RosteredShiftEntity> {

    @NonNull
    @Override
    ItemViewModelContract<RosteredShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

}
