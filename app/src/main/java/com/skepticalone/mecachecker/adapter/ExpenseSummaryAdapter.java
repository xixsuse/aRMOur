package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseSummaryAdapter extends ItemSummaryAdapter<ExpenseEntity> {

    private boolean includeUnclaimed = true, includeClaimed = true, includePaid = true;
    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    public void includeUnclaimed(boolean includeUnclaimed) {
        if (this.includeUnclaimed != includeUnclaimed) {
            this.includeUnclaimed = includeUnclaimed;
            onFiltersChanged();
        }
    }

    public void includeClaimed(boolean includeClaimed) {
        if (this.includeClaimed != includeClaimed) {
            this.includeClaimed = includeClaimed;
            onFiltersChanged();
        }
    }

    public void includePaid(boolean includePaid) {
        if (this.includePaid != includePaid) {
            this.includePaid = includePaid;
            onFiltersChanged();
        }
    }

    private boolean filtered() {
        return !includeUnclaimed || !includeClaimed || !includePaid;
    }

    @Override
    boolean bindViewHolder(@NonNull List<ExpenseEntity> items, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final String secondLine;
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            icon = R.drawable.ic_list_black_24dp;
            firstLine = R.string.expenses;
            int filteredCount = 0;
            for (ExpenseEntity expense : items) {
                PaymentData paymentData = expense.getPaymentData();
                if (paymentData.getClaimed() == null) {
                    if (!includeUnclaimed) continue;
                } else if (paymentData.getPaid() == null) {
                    if (!includeClaimed) continue;
                } else {
                    if (!includePaid) continue;
                }
                filteredCount++;
            }
            secondLine = (filtered() && items.size() > 0) ? holder.getCountPercentage(filteredCount, filteredCount * 100f / items.size()) : holder.getCount(filteredCount);
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            icon = R.drawable.ic_dollar_black_24dp;
            firstLine = R.string.total_payment;
            BigDecimal filteredPayment = BigDecimal.ZERO, totalPayment = BigDecimal.ZERO;
            for (ExpenseEntity expense : items) {
                PaymentData paymentData = expense.getPaymentData();
                totalPayment = totalPayment.add(paymentData.getPayment());
                if (paymentData.getClaimed() == null) {
                    if (!includeUnclaimed) continue;
                } else if (paymentData.getPaid() == null) {
                    if (!includeClaimed) continue;
                } else {
                    if (!includePaid) continue;
                }
                filteredPayment = filteredPayment.add(paymentData.getPayment());
            }
            secondLine = (filtered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) ? holder.getPaymentPercentage(filteredPayment, filteredPayment.multiply(new BigDecimal(100)).divide(totalPayment, BigDecimal.ROUND_HALF_UP)) : holder.getPaymentText(filteredPayment);
        } else return false;
        holder.setupPlain(icon, null);
        holder.setText(holder.getText(firstLine), secondLine);
        return true;
    }
}
