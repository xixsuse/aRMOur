package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getCurrentItem().observe(this, new Observer<ExpenseEntity>() {
            @Override
            public void onChanged(@Nullable ExpenseEntity expenseEntity) {
                if (expenseEntity != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    showDetail(expenseEntity.getId());
                }
            }
        });
    }

}
