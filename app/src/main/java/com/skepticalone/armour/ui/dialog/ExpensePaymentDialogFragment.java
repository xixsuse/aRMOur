package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;

public final class ExpensePaymentDialogFragment extends PaymentDialogFragment<Expense> {

    private PayableViewModelContract<Expense> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    PayableViewModelContract<Expense> getViewModel() {
        return viewModel;
    }


    @Override
    int getTitle() {
        return R.string.payment;
    }

}
