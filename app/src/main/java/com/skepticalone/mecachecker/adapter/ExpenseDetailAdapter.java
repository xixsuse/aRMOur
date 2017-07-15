package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public class ExpenseDetailAdapter extends PayableItemDetailAdapter<Expense> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks mCallbacks;

    public ExpenseDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        mCallbacks = callbacks;
    }

    @Override
    int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
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
    int getRowCount(@NonNull Expense item) {
        return ROW_COUNT;
    }

    @Override
    void onItemUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        super.onItemUpdated(oldExpense, newExpense);
        if (!Comparators.equalStrings(oldExpense.getTitle(), newExpense.getTitle())) {
            notifyItemChanged(ROW_NUMBER_TITLE);
        }
    }

    @Override
    void bindViewHolder(@NonNull final Expense expense, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TITLE) {
            holder.setupPlain(R.drawable.ic_title_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.changeTitle(expense.getId(), expense.getTitle());
                }
            });
            holder.setText(holder.getText(R.string.title), expense.getTitle());
        } else {
            super.bindViewHolder(expense, holder, position);
        }
    }

    public interface Callbacks extends PayableItemDetailAdapter.Callbacks {
        void changeTitle(long id, @NonNull String currentTitle);
    }

}
