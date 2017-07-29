package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class AdditionalShiftCommentDialogFragment extends CommentDialogFragment<AdditionalShiftEntity> {

    @NonNull
    @Override
    ViewModelContract<AdditionalShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

}
