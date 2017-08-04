package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class ExpenseTotalsDialogFragment extends PayableTotalsDialogFragment<ExpenseEntity> {
    @NonNull
    @Override
    ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @Override
    public int getTitle() {
        return R.string.expenses;
    }
}
