package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;

public final class ExpenseTitleDialogFragment extends PlainTextDialogFragment<Expense> {

    private ExpenseViewModel viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(ExpenseViewModel.class);
    }

    @NonNull
    @Override
    ExpenseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    int getTitle() {
        return R.string.title;
    }

    @Override
    String getTextForDisplay(@NonNull Expense item) {
        return item.getTitle();
    }

    @Override
    void saveText(@Nullable String title) {
        if (title == null) {
            showSnackbar(R.string.title_required);
        } else {
            getViewModel().saveNewTitle(title);
        }
    }
}
