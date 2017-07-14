package com.skepticalone.mecachecker.ui;


import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.ExpenseEntity;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.model.Expense;

public class ExpenseListFragment extends SingleAddListFragment<Expense, ExpenseEntity, ExpenseViewModel> {

    @NonNull
    @Override
    ItemListAdapter<Expense> onCreateAdapter() {
        return new ExpenseListAdapter(this);
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @Override
    Class<ExpenseViewModel> getViewModelClass() {
        return ExpenseViewModel.class;
    }

}
