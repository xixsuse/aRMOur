package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.RawExpenseEntity;
import com.skepticalone.armour.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<RawExpenseEntity> {

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
    boolean areContentsTheSame(@NonNull RawExpenseEntity expense1, @NonNull RawExpenseEntity expense2) {
        return super.areContentsTheSame(expense1, expense2) &&
                Comparators.equalPaymentData(expense1.getData(), expense2.getData()) &&
                expense1.getTitle().equals(expense2.getTitle());
    }

    @Override
    void bindViewHolder(@NonNull RawExpenseEntity expense, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(expense.getData().getIcon());
        holder.setText(expense.getTitle(), holder.getPaymentString(expense.getTotalPayment()), expense.getComment());
    }

}
