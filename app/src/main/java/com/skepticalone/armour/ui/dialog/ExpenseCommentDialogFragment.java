package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawExpenseEntity;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseCommentDialogFragment extends CommentDialogFragment<RawExpenseEntity> {

    @NonNull
    @Override
    ItemViewModelContract<RawExpenseEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

}
