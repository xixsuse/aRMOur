package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class RosteredShiftCommentDialogFragment extends CommentDialogFragment<RosteredShiftEntity> {

    @NonNull
    @Override
    ViewModelContract<RosteredShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(RosteredShiftViewModel.class);
    }

}
