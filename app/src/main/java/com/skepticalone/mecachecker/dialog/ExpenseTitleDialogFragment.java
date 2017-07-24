package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Expense;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;

public final class ExpenseTitleDialogFragment extends IndependentPlainTextDialogFragment<Expense, TitleViewModel<Expense>> {

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
    TitleViewModel<Expense> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(NewExpenseViewModel.class);
    }

    @Override
    void onCurrentItemChanged(@NonNull Expense item) {
        setText(item.getTitle());
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
