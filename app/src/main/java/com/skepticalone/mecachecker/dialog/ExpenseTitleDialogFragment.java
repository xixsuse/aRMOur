package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.TitleViewModelContract;

public final class ExpenseTitleDialogFragment extends PlainTextDialogFragment<ExpenseEntity, TitleViewModelContract> {

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
    TitleViewModelContract onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ExpenseViewModel.class);
    }

    @Override
    String getTextForDisplay(@NonNull ExpenseEntity item) {
        return item.getTitle();
    }

    @Override
    void saveText(@Nullable String title) {
        if (title == null) {
            showSnackbar(R.string.value_required);
        } else {
            getViewModel().saveNewTitle(title);
        }
    }
}
