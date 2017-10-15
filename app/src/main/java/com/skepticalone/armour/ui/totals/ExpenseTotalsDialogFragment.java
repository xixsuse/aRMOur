package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<Expense> {

    public static ExpenseTotalsDialogFragment newInstance(boolean selected) {
        Bundle args = new Bundle();
        args.putBoolean(SELECTED, selected);
        ExpenseTotalsDialogFragment fragment = new ExpenseTotalsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ItemTotalsAdapter<Expense> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(context, isSelected() ? R.string.selected_expenses : R.string.all_expenses, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<Expense> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

}
