package com.skepticalone.mecachecker.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public class ExpenseDetailAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;
    private final ExpenseUpdateCallback mDetailCallback;
    private Expense mExpense;

    public ExpenseDetailAdapter(ExpenseUpdateCallback callback) {
        super();
        mDetailCallback = callback;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemViewHolder holder = new ItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        int primaryIcon;
        boolean switchVisible = false, switchChecked = false;
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;
        switch (position) {
            case ROW_NUMBER_TITLE:
                primaryIcon = R.drawable.ic_title_black_24dp;
                holder.setText(R.string.title, mExpense.getTitle());
                break;
            case ROW_NUMBER_PAYMENT:
                primaryIcon = R.drawable.ic_dollar_black_24dp;
                holder.setText(R.string.payment, R.string.currency_format, mExpense.getPayment());
                break;
            case ROW_NUMBER_COMMENT:
                primaryIcon = R.drawable.ic_comment_black_24dp;
                holder.setText(R.string.comment, mExpense.getComment());
                break;
            case ROW_NUMBER_CLAIMED:
                switchVisible = true;
                DateTime claimed = mExpense.getClaimed();
                if (claimed == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.claimed, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_half_black_24dp;
                    holder.setText(R.string.claimed, DateTimeUtils.getDateTimeString(claimed));
                    switchChecked = true;
                }
                onCheckedChangeListener = mExpense.getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                        mDetailCallback.setClaimed(mExpense.getId(), claimed);
                    }
                } : null;
                break;
            case ROW_NUMBER_PAID:
                switchVisible = true;
                final DateTime paid = mExpense.getPaid();
                if (paid == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.paid, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_full_black_24dp;
                    holder.setText(R.string.paid, DateTimeUtils.getDateTimeString(paid));
                    switchChecked = true;
                }
                onCheckedChangeListener = mExpense.getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                        mDetailCallback.setPaid(mExpense.getId(), paid);
                    }
                };
                break;
            default:
                throw new IllegalStateException();
        }
        holder.primaryIcon.setImageResource(primaryIcon);
        if (switchVisible) {
            if (holder.switchControl.isChecked() != switchChecked) {
                holder.switchControl.setOnCheckedChangeListener(null);
                holder.switchControl.setChecked(switchChecked);
            }
            holder.switchControl.setEnabled(onCheckedChangeListener != null);
            holder.switchControl.setVisibility(View.VISIBLE);
        } else {
            holder.switchControl.setVisibility(View.GONE);
        }
        holder.switchControl.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    public int getItemCount() {
        return mExpense == null ? 0 : ROW_COUNT;
    }

    public void setExpense(final Expense expense) {
        if (mExpense == null) {
            notifyItemRangeInserted(0, ROW_COUNT);
        } else {
            if (!Comparators.equalStrings(mExpense.getTitle(), expense.getTitle())) {
                notifyItemChanged(ROW_NUMBER_TITLE);
            }
            if (!Comparators.equalBigDecimals(mExpense.getPayment(), expense.getPayment())) {
                notifyItemChanged(ROW_NUMBER_PAYMENT);
            }
            if (!Comparators.equalStrings(mExpense.getComment(), expense.getComment())) {
                notifyItemChanged(ROW_NUMBER_COMMENT);
            }
            if (!Comparators.equalDateTimes(mExpense.getClaimed(), expense.getClaimed()) || !Comparators.equalDateTimes(mExpense.getPaid(), expense.getPaid())) {
                notifyItemChanged(ROW_NUMBER_CLAIMED);
                notifyItemChanged(ROW_NUMBER_PAID);
            }
        }
        mExpense = expense;
    }

}
