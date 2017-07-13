package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return
                Comparators.equalStrings(expense1.getTitle(), expense2.getTitle()) &&
                        Comparators.equalPaymentData(expense1.getPaymentData(), expense2.getPaymentData());
    }

    @Override
    void bindViewHolder(@NonNull Expense expense, ItemViewHolder holder) {
        holder.setText(expense.getTitle(), expense.getPaymentData().getComment());
        holder.secondaryIcon.setImageResource(expense.getPaymentData().getIcon());
    }

}
