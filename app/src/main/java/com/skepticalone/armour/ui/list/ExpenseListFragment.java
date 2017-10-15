package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ExpenseListAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.ui.totals.ExpenseTotalsDialogFragment;

public final class ExpenseListFragment extends SingleAddListFragment<Expense> {

    private ExpenseListAdapter adapter;
    private ExpenseViewModel viewModel;

    @Override
    int getItemType() {
        return R.id.expenses;
    }

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new ExpenseListAdapter(context, this);
    }

    @NonNull
    @Override
    protected ExpenseListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    protected ExpenseViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    ExpenseTotalsDialogFragment createSummaryDialogFragment(boolean subtotals) {
        return ExpenseTotalsDialogFragment.newInstance(subtotals);
    }

}
