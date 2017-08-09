package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.entity.ExpenseEntity;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;

public final class ExpenseTitleDialogFragment extends PlainTextDialogFragment<ExpenseEntity> {

    @Override
    int getTitle() {
        return R.string.title;
    }

    @NonNull
    @Override
    ExpenseViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @Override
    String getTextForDisplay(@NonNull ExpenseEntity item) {
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
