package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;

public final class ExpensePaymentDialogFragment extends PaymentDialogFragment<ExpenseEntity> {

    @Override
    int getTitle() {
        return R.string.payment;
    }

    @NonNull
    @Override
    PayableViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

}
