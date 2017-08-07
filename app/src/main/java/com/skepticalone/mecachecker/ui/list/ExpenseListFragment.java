package com.skepticalone.mecachecker.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.ui.totals.ExpenseTotalsDialogFragment;

public final class ExpenseListFragment extends SingleAddListFragment<ExpenseEntity> {

    private final ExpenseListAdapter adapter = new ExpenseListAdapter(this);

    @Override
    int getItemType() {
        return R.id.expenses;
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
