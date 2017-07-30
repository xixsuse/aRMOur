package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.ExpenseTitleDialogFragment;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;

public final class ExpenseDetailFragment
        extends PayableDetailFragment<ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks {

    @NonNull
    @Override
    PayableDetailAdapter<ExpenseEntity> createAdapter(Context context) {
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

    @NonNull
    @Override
    PaymentDialogFragment getNewPaymentDialogFragment() {
        return new ExpensePaymentDialogFragment();
    }

}
