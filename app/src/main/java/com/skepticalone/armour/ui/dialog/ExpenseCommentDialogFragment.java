package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseCommentDialogFragment extends CommentDialogFragment<Expense> {

    private ItemViewModelContract<Expense> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    ItemViewModelContract<Expense> getViewModel() {
        return viewModel;
    }

}
