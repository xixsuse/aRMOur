package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.Expense;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;

public final class ExpenseCommentDialogFragment extends IndependentCommentDialogFragment<Expense> {

    @NonNull
    @Override
    CommentViewModel<Expense> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

}
