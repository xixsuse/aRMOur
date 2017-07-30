package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<ExpenseEntity> {

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
    boolean areContentsTheSame(@NonNull ExpenseEntity expense1, @NonNull ExpenseEntity expense2) {
        return super.areContentsTheSame(expense1, expense2) &&
                Comparators.equalPaymentData(expense1.getPaymentData(), expense2.getPaymentData()) &&
                expense1.getTitle().equals(expense2.getTitle());
    }

    @Override
    void bindViewHolder(@NonNull ExpenseEntity expense, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(expense.getPaymentData().getIcon());
        holder.setText(expense.getTitle(), holder.getCurrencyText(expense.getPaymentData().getPayment()), expense.getComment());
    }

}
