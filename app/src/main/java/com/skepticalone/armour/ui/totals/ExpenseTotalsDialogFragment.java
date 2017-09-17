package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.entity.ExpenseEntity;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<ExpenseEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<ExpenseEntity> createAdapter() {
        return new SinglePayableTotalsAdapter<>(this, R.string.expenses);
    }

    @NonNull
    @Override
    ItemViewModelContract<ExpenseEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

}
