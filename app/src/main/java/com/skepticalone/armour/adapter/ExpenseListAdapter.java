package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.primaryIcon.setVisibility(View.GONE);
        return viewHolder;
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return expense1.getTitle().equals(expense2.getTitle()) &&
                expense1.getTotalPayment().equals(expense2.getTotalPayment()) &&
                Comparators.equalStrings(expense1.getComment(), expense2.getComment()) &&
                expense1.getPaymentData().getIcon() == expense2.getPaymentData().getIcon();
    }

    @Override
    void bindViewHolder(@NonNull Expense expense, ItemViewHolder holder) {
        holder.setText(expense.getTitle(), holder.getPaymentString(expense.getTotalPayment()), expense.getComment());
        holder.secondaryIcon.setImageResource(expense.getPaymentData().getIcon());
    }

}
