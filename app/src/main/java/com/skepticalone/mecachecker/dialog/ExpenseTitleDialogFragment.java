package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.TitleViewModel;

public final class ExpenseTitleDialogFragment extends IndependentPlainTextDialogFragment<ExpenseEntity, TitleViewModel> {

    @Override
    int getTitle() {
        return R.string.title;
    }

    @Override
    int getHint() {
        return super.getHint();
    }

    @NonNull
    @Override
    TitleViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(NewExpenseViewModel.class);
    }

    @Override
    String getTextForDisplay(@NonNull ExpenseEntity item) {
        return item.getTitle();
    }

    @Override
    void saveText(@Nullable String title) {
        if (title == null) {
            // TODO: 24/07/17
        } else {
            getViewModel().saveNewTitle(title);
        }
    }
}
