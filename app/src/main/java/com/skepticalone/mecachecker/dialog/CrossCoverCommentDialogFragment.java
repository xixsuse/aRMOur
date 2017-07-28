package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class CrossCoverCommentDialogFragment extends CommentDialogFragment<CrossCoverEntity> {

    @NonNull
    @Override
    ViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(CrossCoverViewModel.class);
    }

}
