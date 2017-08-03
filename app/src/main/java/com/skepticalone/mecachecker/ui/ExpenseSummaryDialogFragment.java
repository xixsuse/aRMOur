package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseSummaryAdapter;
import com.skepticalone.mecachecker.adapter.ItemSummaryAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class ExpenseSummaryDialogFragment extends SummaryDialogFragment<ExpenseEntity> {

    @NonNull
    @Override
    ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    ItemSummaryAdapter<ExpenseEntity> createAdapter() {
        return new ExpenseSummaryAdapter();
    }

}
