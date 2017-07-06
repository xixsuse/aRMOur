package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areItemsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return expense1.getId() == expense2.getId();
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return
                Comparators.equalStrings(expense1.getTitle(), expense2.getTitle()) &&
                        Comparators.equalStrings(expense1.getComment(), expense2.getComment()) &&
                        Comparators.equalBigDecimals(expense1.getPayment(), expense2.getPayment()) &&
                        Comparators.equalDateTimes(expense1.getClaimed(), expense2.getClaimed()) &&
                        Comparators.equalDateTimes(expense1.getPaid(), expense2.getPaid())
                ;
    }

    @Override
    void bindViewHolder(@NonNull Expense expense, ItemViewHolder holder) {
        holder.setText(expense.getTitle());
        holder.secondaryIcon.setImageResource(expense.getPaid() == null ? expense.getClaimed() == null ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }

}
