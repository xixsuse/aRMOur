package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

public final class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks mCallbacks;
    private final PayableDetailHelper<Expense> mHelper;

    public ExpenseDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        mCallbacks = callbacks;
        mHelper = new PayableDetailHelper<>(callbacks, ROW_NUMBER_PAYMENT, ROW_NUMBER_CLAIMED, ROW_NUMBER_PAID);
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
    void onItemUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        super.onItemUpdated(oldExpense, newExpense);
        mHelper.onItemUpdated(oldExpense, newExpense, this);
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
                    mCallbacks.changeTitle(expense.getId(), expense.getTitle());
                }
            });
            holder.setText(holder.getText(R.string.title), expense.getTitle());
            return true;
        } else return mHelper.bindViewHolder(expense, holder, position) || super.bindViewHolder(expense, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, PayableDetailFragmentCallbacks {
        void changeTitle(long id, @NonNull String currentTitle);
    }

}
