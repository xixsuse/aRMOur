package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    private final Callbacks callbacks;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public ExpenseDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks);
    }

    @Override
    void onItemUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        super.onItemUpdated(oldExpense, newExpense);
        payableDetailAdapterHelper.onItemUpdated(oldExpense, newExpense, this);
        if (!Comparators.equalStrings(oldExpense.getTitle(), newExpense.getTitle())) {
            notifyItemChanged(callbacks.getRowNumberTitle());
        }
    }

    @Override
    boolean bindViewHolder(@NonNull final Expense expense, ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberTitle()) {
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

    public interface Callbacks extends ItemDetailAdapter.Callbacks<Expense>, PayableDetailAdapterHelper.Callbacks {
        int getRowNumberTitle();
        void changeTitle(long id, @NonNull String currentTitle);
    }

}
