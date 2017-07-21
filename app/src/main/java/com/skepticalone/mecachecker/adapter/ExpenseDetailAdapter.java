package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks callbacks;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public ExpenseDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks) {
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
    int getRowCount(@NonNull Expense item) {
        return ROW_COUNT;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        super.onItemUpdated(oldExpense, newExpense);
        payableDetailAdapterHelper.onItemUpdated(oldExpense, newExpense, this);
        if (!Comparators.equalStrings(oldExpense.getTitle(), newExpense.getTitle())) {
            notifyItemChanged(ROW_NUMBER_TITLE);
        }
    }

    @Override
    boolean bindViewHolder(@NonNull final Expense expense, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TITLE) {
            holder.setupPlain(R.drawable.ic_title_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTitle(expense.getId(), expense.getTitle());
                }
            });
            holder.setText(holder.getText(R.string.title), expense.getTitle());
            return true;
        } else return payableDetailAdapterHelper.bindViewHolder(expense, holder, position) || super.bindViewHolder(expense, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, PayableDetailAdapterHelper.Callbacks {
        void changeTitle(long id, @NonNull String currentTitle);
    }

}
