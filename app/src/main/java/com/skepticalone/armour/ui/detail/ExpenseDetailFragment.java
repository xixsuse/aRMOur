package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.ExpenseDetailAdapter;
import com.skepticalone.armour.data.entity.ExpenseEntity;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpenseCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpenseTitleDialogFragment;
import com.skepticalone.armour.ui.dialog.PaymentDialogFragment;

public final class ExpenseDetailFragment
        extends PayableDetailFragment<ExpenseEntity>
        implements ExpenseDetailAdapter.Callbacks {

    private final ExpenseDetailAdapter adapter = new ExpenseDetailAdapter(this);

    @NonNull
    @Override
    protected ExpenseDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected ExpenseViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @Override
    public void changeTitle() {
        showDialogFragment(new ExpenseTitleDialogFragment());
    }

    @NonNull
    @Override
    PaymentDialogFragment<ExpenseEntity> createPaymentDialogFragment() {
        return new ExpensePaymentDialogFragment();
    }

    @NonNull
    @Override
    CommentDialogFragment<ExpenseEntity> createCommentDialogFragment() {
        return new ExpenseCommentDialogFragment();
    }

}
