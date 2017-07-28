package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.ExpenseTitleDialogFragment;

public final class ExpenseDetailFragment
        extends DetailFragment<ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
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
    public void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

}
