package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseListAdapter;
import com.skepticalone.mecachecker.adapter.ExpenseTotalsAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

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
    ExpenseViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<ExpenseEntity> createSummaryDialogFragment() {
        return new ExpenseTotalsDialogFragment();
    }

    public static final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<ExpenseEntity> {

        @NonNull
        @Override
        ItemTotalsAdapter<ExpenseEntity> createAdapter(@NonNull Context context) {
            return new ExpenseTotalsAdapter(this);
        }

        @NonNull
        @Override
        ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
            return provider.get(ExpenseViewModel.class);
        }

    }
}
