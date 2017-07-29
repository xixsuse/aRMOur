package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;

public final class ExpenseListFragment extends SingleAddListFragment<ExpenseEntity, ExpenseViewModel> {

    @Override
    int getItemType() {
        return R.id.expenses;
    }

    @NonNull
    @Override
    ItemListAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseListAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

}
