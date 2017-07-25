package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;

public final class ExpensePaymentDialogFragment extends PaymentDialogFragment<ExpenseEntity, ExpenseViewModel> {

    @Override
    int getTitle() {
        return R.string.payment;
    }

    @NonNull
    @Override
    ExpenseViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

}
