package com.skepticalone.mecachecker.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public class ExpenseDetailAdapter extends RecyclerView.Adapter<ExpenseDetailAdapter.ExpenseDetailItemViewHolder> {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;
    private Expense mExpense;

    @Override
    public ExpenseDetailItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ExpenseDetailItemViewHolder holder = new ExpenseDetailItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        holder.switchControl.setVisibility(View.GONE);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExpenseDetailItemViewHolder holder, int position) {
        int primaryIcon;
        String text;
        switch (position) {
            case ROW_NUMBER_TITLE:
                primaryIcon = R.drawable.ic_title_black_24dp;
                text = mExpense.getTitle();
                break;
            case ROW_NUMBER_PAYMENT:
                primaryIcon = R.drawable.ic_dollar_black_24dp;
                text = holder.text.getContext().getString(R.string.currency_format, mExpense.getPayment());
                break;
            case ROW_NUMBER_COMMENT:
                primaryIcon = R.drawable.ic_comment_black_24dp;
                text = mExpense.getComment();
                break;
            case ROW_NUMBER_CLAIMED:
                DateTime claimed = mExpense.getClaimed();
                primaryIcon = claimed == null ? 0 : R.drawable.ic_check_box_half_black_24dp;
                text = claimed == null ? holder.text.getContext().getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(claimed);
                break;
            case ROW_NUMBER_PAID:
                DateTime paid = mExpense.getPaid();
                primaryIcon = paid == null ? 0 : R.drawable.ic_check_box_full_black_24dp;
                text = paid == null ? holder.text.getContext().getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(paid);
                break;
            default:
                throw new IllegalStateException();
        }
        holder.primaryIcon.setImageResource(primaryIcon);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return mExpense == null ? 0 : ROW_COUNT;
    }

    public void setExpense(final Expense expense) {
        if (mExpense == null) {
            notifyItemRangeInserted(0, ROW_COUNT);
        } else {
            notifyItemRangeChanged(0, ROW_COUNT);
        }
        mExpense = expense;
    }

    static class ExpenseDetailItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView primaryIcon, secondaryIcon;
        private final TextView text;
        private final SwitchCompat switchControl;

        ExpenseDetailItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            primaryIcon = itemView.findViewById(R.id.primary_icon);
            text = itemView.findViewById(R.id.text);
            secondaryIcon = itemView.findViewById(R.id.secondary_icon);
            switchControl = itemView.findViewById(R.id.switch_control);
        }
    }

}
