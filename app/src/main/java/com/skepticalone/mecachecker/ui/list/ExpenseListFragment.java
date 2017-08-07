package com.skepticalone.mecachecker.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.ui.TotalsDialogFragment;
import com.skepticalone.mecachecker.ui.totals.ExpenseTotalsDialogFragment;

public final class ExpenseListFragment extends SingleAddListFragment<ExpenseEntity> {

    private final ExpenseListAdapter adapter = new ExpenseListAdapter(this);

    @Override
    int getItemType() {
        return R.id.expenses;
    }

    @NonNull
    @Override
    ItemListAdapter<ExpenseEntity> getAdapter() {
        return adapter;
    }

    @Override
    void addNewItem() {
        getViewModel().addNewExpense();
    }

    @NonNull
    @Override
    ExpenseViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<ExpenseEntity> createSummaryDialogFragment() {
        return new ExpenseTotalsDialogFragment();
    }

}
