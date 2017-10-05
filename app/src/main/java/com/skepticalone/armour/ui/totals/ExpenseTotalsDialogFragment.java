package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<Expense> {

    @NonNull
    @Override
    ItemTotalsAdapter<Expense> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(context, this, R.string.expenses);
    }

    @NonNull
    @Override
    ItemViewModelContract<Expense> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

}
