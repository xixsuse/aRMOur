package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.ExpenseEntity;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.dialog.TitleDialogFragment;
import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseDetailFragment
        extends PayableDetailFragment<Expense, ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks, TitleDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<Expense> createAdapter() {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
    }

    @Override
    public void changeTitle(long id, @NonNull String currentTitle) {
        showDialogFragment(TitleDialogFragment.newInstance(id, currentTitle));
    }

    @Override
    public void saveTitle(long itemId, @NonNull String trimmedTitle) {
        getViewModel().setTitle(itemId, trimmedTitle);
    }

}
