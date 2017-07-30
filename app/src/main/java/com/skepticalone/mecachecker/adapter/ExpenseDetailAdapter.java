package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseDetailAdapter extends PayableDetailAdapter<ExpenseEntity> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    @NonNull
    private final Callbacks callbacks;

    public ExpenseDetailAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
    }

    @Override
    int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    int getRowNumberComment(@NonNull ExpenseEntity expense) {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull ExpenseEntity expense) {
        return ROW_COUNT;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull ExpenseEntity oldExpense, @NonNull ExpenseEntity newExpense) {
        super.onItemUpdated(oldExpense, newExpense);
        if (!Comparators.equalStrings(oldExpense.getTitle(), newExpense.getTitle())) {
            notifyItemChanged(ROW_NUMBER_TITLE);
        }
    }

    @Override
    boolean bindViewHolder(@NonNull ExpenseEntity expense, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TITLE) {
            holder.setupPlain(R.drawable.ic_title_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTitle();
                }
            });
            holder.setText(holder.getText(R.string.title), expense.getTitle());
            return true;
        } else return super.bindViewHolder(expense, holder, position);
    }

    public interface Callbacks extends PayableDetailAdapter.Callbacks {
        void changeTitle();
    }

}
