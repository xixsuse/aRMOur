package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.FilteredItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class ExpenseTotalsDialogFragment extends PayableItemTotalsDialogFragment<Expense> {

    public static ExpenseTotalsDialogFragment newInstance(boolean selected) {
        ExpenseTotalsDialogFragment fragment = new ExpenseTotalsDialogFragment();
        fragment.setArguments(getArgs(selected));
        return fragment;
    }

    @NonNull
    @Override
    FilteredItemTotalsAdapter<Expense> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(context, isSelected() ? R.string.selected_expenses : R.string.all_expenses, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<Expense> getViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

}
