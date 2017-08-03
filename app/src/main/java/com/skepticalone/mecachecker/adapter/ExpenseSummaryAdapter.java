package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseSummaryAdapter extends ItemSummaryAdapter<ExpenseEntity> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    boolean bindViewHolder(@NonNull List<ExpenseEntity> items, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final String secondLine;
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            icon = R.drawable.ic_list_black_24dp;
            firstLine = R.string.expenses;
            secondLine = Integer.toString(items.size());
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            icon = R.drawable.ic_dollar_black_24dp;
            firstLine = R.string.total_payment;
            BigDecimal totalPayment = BigDecimal.ZERO;
            for (ExpenseEntity expense : items) {
                totalPayment = totalPayment.add(expense.getPaymentData().getPayment());
            }
            secondLine = holder.getCurrencyText(totalPayment);
        } else return false;
        holder.setupPlain(icon, null);
        holder.setText(holder.getText(firstLine), secondLine);
        return true;
    }
}
