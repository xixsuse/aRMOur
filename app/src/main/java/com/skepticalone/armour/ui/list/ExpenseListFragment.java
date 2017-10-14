package com.skepticalone.armour.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ExpenseListAdapter;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.ui.totals.ExpenseTotalsDialogFragment;

public final class ExpenseListFragment extends SingleAddListFragment<Expense> {

    private ExpenseListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new ExpenseListAdapter(context, this);
    }

    @Override
    int getItemType() {
        return R.id.expenses;
    }

    @Override
    int getQuantityStringResource() {
        return R.plurals.expenses;
    }

    @NonNull
    @Override
    protected ExpenseListAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected ExpenseViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    ExpenseTotalsDialogFragment createSummaryDialogFragment() {
        return new ExpenseTotalsDialogFragment();
    }

}
