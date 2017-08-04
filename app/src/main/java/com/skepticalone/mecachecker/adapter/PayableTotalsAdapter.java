package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Payable;
import com.skepticalone.mecachecker.data.util.PaymentData;

import java.math.BigDecimal;
import java.util.List;

public final class PayableTotalsAdapter<Entity extends Payable> extends ItemTotalsAdapter<Entity> {

    private final Callbacks callbacks;

    public PayableTotalsAdapter(Callbacks callbacks) {
        super();
        this.callbacks = callbacks;
    }

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    private boolean filtered() {
        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
    }

    private boolean isIncluded(@NonNull PaymentData paymentData) {
        if (paymentData.getClaimed() == null) {
            return callbacks.includeUnclaimed();
        } else if (paymentData.getPaid() == null) {
            return callbacks.includeClaimed();
        } else {
            return callbacks.includePaid();
        }
    }

    @Override
    boolean bindViewHolder(@NonNull List<Entity> items, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final String secondLine;
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            icon = R.drawable.ic_list_black_24dp;
            firstLine = callbacks.getTitle();
            int totalCount = items.size();
            if (filtered() && totalCount > 0) {
                int filteredCount = 0;
                for (Payable item : items) {
                    if (isIncluded(item.getPaymentData())) filteredCount++;
                }
                secondLine = holder.getCountPercentage(filteredCount, totalCount);
            } else {
                secondLine = holder.getCount(totalCount);
            }
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            icon = R.drawable.ic_dollar_black_24dp;
            firstLine = R.string.total_payment;
            BigDecimal totalPayment = BigDecimal.ZERO;
            for (Payable item : items) {
                totalPayment = totalPayment.add(item.getPaymentData().getPayment());
            }
            if (filtered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal filteredPayment = BigDecimal.ZERO;
                for (Payable item : items) {
                    PaymentData paymentData = item.getPaymentData();
                    if (isIncluded(paymentData)) filteredPayment = filteredPayment.add(paymentData.getPayment());
                }
                secondLine = holder.getPaymentPercentage(filteredPayment, totalPayment);
            } else {
                secondLine = holder.getPaymentText(totalPayment);
            }
        } else return false;
        holder.setupPlain(icon, null);
        holder.setText(holder.getText(firstLine), secondLine);
        return true;
    }

    public interface Callbacks {
        @StringRes
        int getTitle();
        boolean includeUnclaimed();
        boolean includeClaimed();
        boolean includePaid();
    }

}
