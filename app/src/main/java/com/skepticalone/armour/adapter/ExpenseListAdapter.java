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
        return super.areContentsTheSame(expense1, expense2) &&
                Comparators.equalPaymentData(expense1.getPaymentData(), expense2.getPaymentData()) &&
                expense1.getTitle().equals(expense2.getTitle());
    }

    @Override
    void bindViewHolder(@NonNull Expense expense, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(expense.getPaymentData().getIcon());
        holder.setText(expense.getTitle(), holder.getPaymentString(expense.getTotalPayment()), expense.getComment());
    }

}
