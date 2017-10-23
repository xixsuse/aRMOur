package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;

public final class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final PayableDetailAdapterHelper<Expense> payableDetailAdapterHelper;

    public ExpenseDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
        this.callbacks = callbacks;
        payableDetailAdapterHelper = new PayableDetailAdapterHelper<Expense>(callbacks) {
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
        };
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getFixedRowCount() {
        return ROW_COUNT;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.hideSecondaryIcon();
        return holder;
    }

    @Override
    void notifyUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        super.notifyUpdated(oldExpense, newExpense);
        payableDetailAdapterHelper.onChanged(oldExpense, newExpense, this);
        if (!oldExpense.getTitle().equals(newExpense.getTitle())) {
            notifyItemChanged(ROW_NUMBER_TITLE);
        }
    }

    @Override
    void onBindViewHolder(@NonNull Expense expense, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_TITLE) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_title_black_24dp);
            holder.setText(getContext().getString(R.string.title), expense.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTitle();
                }
            });
        } else if (!payableDetailAdapterHelper.bindViewHolder(expense, position, holder, this)) {
            super.onBindViewHolder(expense, position, holder);
        }
    }

    public interface Callbacks extends PayableDetailAdapterHelper.Callbacks {
        void changeTitle();
    }

}
