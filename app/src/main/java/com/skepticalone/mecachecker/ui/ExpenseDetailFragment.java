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
    public void setClaimed(long itemId, boolean claimed) {
        getViewModel().setClaimed(itemId, claimed);
    }

    @Override
    public void setPaid(long itemId, boolean paid) {
        getViewModel().setPaid(itemId, paid);
    }

    @Override
    public void changeTitle(long itemId, @NonNull String currentTitle) {
        PlainTextDialogFragment fragment = PlainTextDialogFragment.newInstance(itemId, currentTitle, R.string.title);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public void savePlainText(long itemId, @Nullable String trimmedTextWithLength) {
        if (trimmedTextWithLength != null) {
            getViewModel().setTitle(itemId, trimmedTextWithLength);
        }
    }

}
