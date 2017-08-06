package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

public final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<ExpenseEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<ExpenseEntity> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(this, R.string.expenses);
    }

    @NonNull
    @Override
    ItemViewModel<ExpenseEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

}
