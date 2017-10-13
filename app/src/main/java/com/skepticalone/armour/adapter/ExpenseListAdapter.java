package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return expense1.getTitle().equals(expense2.getTitle()) &&
                expense1.getTotalPayment().equals(expense2.getTotalPayment()) &&
                Comparators.equalStrings(expense1.getComment(), expense2.getComment()) &&
                expense1.getPaymentData().getIcon() == expense2.getPaymentData().getIcon();
    }

    @Override
    void bindViewHolder(@NonNull Expense expense, ItemViewHolder holder, boolean selected) {
        holder.primaryIcon.setImageResource(selected ? R.drawable.ic_check_circle_24dp : R.drawable.ic_dollar_black_24dp);
        holder.setText(expense.getTitle(), holder.getPaymentString(expense.getTotalPayment()), expense.getComment());
        holder.secondaryIcon.setImageResource(expense.getPaymentData().getIcon());
    }

}
