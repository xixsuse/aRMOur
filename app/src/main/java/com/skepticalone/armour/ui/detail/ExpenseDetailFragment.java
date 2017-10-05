package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.ExpenseDetailAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpenseCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.armour.ui.dialog.ExpenseTitleDialogFragment;

public final class ExpenseDetailFragment
        extends DetailFragment<Expense>
        implements ExpenseDetailAdapter.Callbacks {

    private ExpenseDetailAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new ExpenseDetailAdapter(context, this);
    }

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

    @Override
    public void changePayment() {
        showDialogFragment(new ExpensePaymentDialogFragment());
    }

    @Override
    public final void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public final void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

    @NonNull
    @Override
    CommentDialogFragment<Expense> createCommentDialogFragment() {
        return new ExpenseCommentDialogFragment();
    }

}
