package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    @NonNull
    private final ExpenseClickCallback mClickCallback;
    private List<? extends Expense> mExpenses;

    public ExpenseAdapter(@NonNull ExpenseClickCallback clickCallback) {
        super();
        setHasStableIds(true);
        mClickCallback = clickCallback;
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
    public final ExpenseViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ExpenseViewHolder viewHolder = new ExpenseViewHolder(parent);
        viewHolder.primaryIcon.setVisibility(View.GONE);
        viewHolder.switchControl.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickCallback.onClick(viewHolder.getItemId());
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense expense = mExpenses.get(position);
        holder.text.setText(expense.getTitle());
        holder.secondaryIcon.setImageResource(expense.getPaid() == null ? expense.getClaimed() == null ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }

    @Override
    public final int getItemCount() {
        return mExpenses == null ? 0 : mExpenses.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ImageView primaryIcon, secondaryIcon;
        private final TextView text;
        private final SwitchCompat switchControl;

        ExpenseViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            primaryIcon = itemView.findViewById(R.id.primary_icon);
            text = itemView.findViewById(R.id.text);
            secondaryIcon = itemView.findViewById(R.id.secondary_icon);
            switchControl = itemView.findViewById(R.id.switch_control);
        }
    }


}
