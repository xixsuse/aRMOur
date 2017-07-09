package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseEntity;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.ui.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.ui.components.PlainTextDialogFragment;

public class ExpenseDetailFragment
        extends DetailFragment<Expense, ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks, PlainTextDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<Expense> onCreateAdapter() {
        return new ExpenseDetailAdapter(this);
    }

    @Override
    Class<ExpenseViewModel> getViewModelClass() {
        return ExpenseViewModel.class;
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        getViewModel().setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        getViewModel().setPaid(id, paid);
    }

    @Override
    public void changeTitle(long id, @NonNull String currentTitle) {
        PlainTextDialogFragment fragment = PlainTextDialogFragment.newInstance(id, currentTitle, R.string.title);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public void savePlainText(long expenseId, @Nullable String trimmedTextWithLength) {
        if (trimmedTextWithLength != null) {
            getViewModel().setTitle(expenseId, trimmedTextWithLength);
        }
    }

}
