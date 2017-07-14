package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.Expense;

public final class ExpenseListAdapter extends PayableItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return super.areContentsTheSame(expense1, expense2) &&
                expense1.getTitle().equals(expense2.getTitle());
    }

    @Override
    String getViewHolderTitle(@NonNull Expense expense) {
        return expense.getTitle();
    }

}
