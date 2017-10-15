package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
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
    private ExpenseViewModel viewModel;

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new ExpenseDetailAdapter(context, this);
    }

    @NonNull
    @Override
    protected ExpenseDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    protected ExpenseViewModel getViewModel() {
        return viewModel;
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
