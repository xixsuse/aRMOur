package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;

public final class ExpenseListFragment extends SingleAddListFragment<ExpenseEntity, NewExpenseViewModel> {

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @Override
    void addNewItem() {
        getViewModel().addNewExpense();
    }

    @NonNull
    @Override
    ItemListAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseListAdapter(this);
    }

    @NonNull
    @Override
    NewExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(NewExpenseViewModel.class);
    }

}
