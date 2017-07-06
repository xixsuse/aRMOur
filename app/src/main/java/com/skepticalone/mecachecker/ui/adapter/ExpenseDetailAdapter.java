package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks mCallbacks;

    public ExpenseDetailAdapter(Callbacks callbacks) {
        super();
        mCallbacks = callbacks;
    }

    @Override
    void bindViewHolder(@NonNull final Expense expense, ItemViewHolder holder, int position) {
        int primaryIcon;
        boolean switchVisible = false, switchChecked = false;
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;
        View.OnClickListener onClickListener = null;
        switch (position) {
            case ROW_NUMBER_TITLE:
                primaryIcon = R.drawable.ic_title_black_24dp;
                holder.setText(R.string.title, expense.getTitle());
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallbacks.changeTitle(expense.getId(), expense.getTitle());
                    }
                };
                break;
            case ROW_NUMBER_PAYMENT:
                primaryIcon = R.drawable.ic_dollar_black_24dp;
                holder.setText(R.string.payment, R.string.currency_format, expense.getPayment());
                break;
            case ROW_NUMBER_COMMENT:
                primaryIcon = R.drawable.ic_comment_black_24dp;
                holder.setText(R.string.comment, expense.getComment());
                break;
            case ROW_NUMBER_CLAIMED:
                switchVisible = true;
                DateTime claimed = expense.getClaimed();
                if (claimed == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.claimed, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_half_black_24dp;
                    holder.setText(R.string.claimed, DateTimeUtils.getDateTimeString(claimed));
                    switchChecked = true;
                }
                onCheckedChangeListener = expense.getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                        mCallbacks.setClaimed(expense.getId(), claimed);
                    }
                } : null;
                break;
            case ROW_NUMBER_PAID:
                switchVisible = true;
                final DateTime paid = expense.getPaid();
                if (paid == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.paid, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_full_black_24dp;
                    holder.setText(R.string.paid, DateTimeUtils.getDateTimeString(paid));
                    switchChecked = true;
                }
                onCheckedChangeListener = expense.getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                        mCallbacks.setPaid(expense.getId(), paid);
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
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    int getRowCount(@NonNull Expense item) {
        return ROW_COUNT;
    }

    @Override
    void onItemUpdated(@NonNull Expense oldExpense, @NonNull Expense newExpense) {
        if (!Comparators.equalStrings(oldExpense.getTitle(), newExpense.getTitle())) {
            notifyItemChanged(ROW_NUMBER_TITLE);
        }
        if (!Comparators.equalBigDecimals(oldExpense.getPayment(), newExpense.getPayment())) {
            notifyItemChanged(ROW_NUMBER_PAYMENT);
        }
        if (!Comparators.equalStrings(oldExpense.getComment(), newExpense.getComment())) {
            notifyItemChanged(ROW_NUMBER_COMMENT);
        }
        if (!Comparators.equalDateTimes(oldExpense.getClaimed(), newExpense.getClaimed()) || !Comparators.equalDateTimes(oldExpense.getPaid(), newExpense.getPaid())) {
            notifyItemChanged(ROW_NUMBER_CLAIMED);
            notifyItemChanged(ROW_NUMBER_PAID);
        }
    }

    public interface Callbacks {
        void setClaimed(long id, boolean claimed);

        void setPaid(long id, boolean paid);

        void changeTitle(long id, @NonNull String currentTitle);
    }

}
