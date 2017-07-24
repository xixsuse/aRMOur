package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.BaseViewModel;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;

public final class ExpenseCommentDialogFragment extends IndependentCommentDialogFragment<ExpenseEntity> {

    @NonNull
    @Override
    BaseViewModel<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(NewExpenseViewModel.class);
    }

}
