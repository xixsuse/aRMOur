package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    @NonNull
    private final ExpenseClickCallback mClickCallback;
    @NonNull
    private final ExpenseDeleteCallback mExpenseDeleteCallback;
    private List<? extends Expense> mExpenses;

    public ExpenseAdapter(@NonNull ExpenseClickCallback clickCallback, @NonNull ExpenseDeleteCallback expenseDeleteCallback) {
        super();
        setHasStableIds(true);
        mClickCallback = clickCallback;
        mExpenseDeleteCallback = expenseDeleteCallback;
    }

    @Override
    public long getItemId(int position) {
        return mExpenses.get(position).getId();
    }

    public void setExpenses(final List<? extends Expense> expenses) {
        if (mExpenses == null) {
            notifyItemRangeInserted(0, expenses.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mExpenses.size();
                }

                @Override
                public int getNewListSize() {
                    return expenses.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Comparators.areItemsTheSame(mExpenses.get(oldItemPosition), expenses.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return Comparators.areContentsTheSame(mExpenses.get(oldItemPosition), expenses.get(newItemPosition));
                }

            });
            result.dispatchUpdatesTo(this);
        }
        mExpenses = expenses;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ItemViewHolder viewHolder = new ItemViewHolder(parent);
        viewHolder.primaryIcon.setVisibility(View.GONE);
        viewHolder.switchControl.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickCallback.onClick(viewHolder.getItemId());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mExpenseDeleteCallback.deleteExpense(viewHolder.getItemId());
                return true;
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Expense expense = mExpenses.get(position);
        holder.setText(expense.getTitle());
        holder.secondaryIcon.setImageResource(expense.getPaid() == null ? expense.getClaimed() == null ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }

    @Override
    public final int getItemCount() {
        return mExpenses == null ? 0 : mExpenses.size();
    }


}
