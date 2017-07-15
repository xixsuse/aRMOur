package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.ExpenseEntity;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.data.Model;
import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseListFragment extends SingleAddListFragment<Expense, ExpenseEntity> {

    private ExpenseViewModel model;

    @NonNull
    @Override
    ItemListAdapter<Expense> createAdapter() {
        return new ExpenseListAdapter(this);
    }

    @Override
    void onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    Model<ExpenseEntity> getViewModel() {
        return model;
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @Override
    void addNewItem() {
        model.addNewItem();
    }
}
