package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class ExpenseCommentDialogFragment extends CommentDialogFragment<ExpenseEntity> {

    @NonNull
    @Override
    ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

}
