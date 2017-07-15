package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.ExpenseEntity;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseListFragment extends SingleAddListFragment<Expense, ExpenseEntity, ExpenseViewModel> {

    @NonNull
    @Override
    ItemListAdapter<Expense> createAdapter() {
        return new ExpenseListAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

}
