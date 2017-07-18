package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseListFragment extends SingleAddListFragment<Expense, ExpenseEntity> {

    private ExpenseViewModel model;

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @NonNull
    @Override
    Model<ExpenseEntity> onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        return model;
    }

    @NonNull
    @Override
    ItemListAdapter<Expense> onCreateAdapter(Context context) {
        return new ExpenseListAdapter(this);
    }

    @Override
    void addNewItem() {
        model.addNewItem();
    }
}
