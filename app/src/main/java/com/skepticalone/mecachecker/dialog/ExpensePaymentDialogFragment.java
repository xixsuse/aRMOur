package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;

public final class ExpensePaymentDialogFragment extends IndependentPaymentDialogFragment<ExpenseEntity, NewExpenseViewModel> {

    @Override
    int getTitle() {
        return R.string.payment;
    }

    @NonNull
    @Override
    NewExpenseViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(NewExpenseViewModel.class);
    }

}
