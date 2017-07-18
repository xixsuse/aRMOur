package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseListFragment extends SingleAddListFragment<Expense, ExpenseEntity, ExpenseViewModel> {

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @NonNull
    @Override
    ItemListAdapter<Expense> createAdapter(Context context) {
        return new ExpenseListAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

}
