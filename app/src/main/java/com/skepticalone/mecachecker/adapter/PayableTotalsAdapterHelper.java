package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Payable;

import java.math.BigDecimal;
import java.util.List;

final class PayableTotalsAdapterHelper<Entity extends Payable> implements TotalsAdapterHelper<Entity> {

    @NonNull
    private final Callbacks<Entity> callbacks;

    PayableTotalsAdapterHelper(@NonNull Callbacks<Entity> callbacks) {
        this.callbacks = callbacks;
    }
//
//    private static final int
//            ROW_NUMBER_TOTAL_NUMBER = 0,
//            ROW_NUMBER_TOTAL_PAYMENT = 1,
//            ROW_COUNT = 2;
//
//    @StringRes
//    abstract int getTitle();
//
//    int getRowNumberTotalNumber() {
//        return ROW_NUMBER_TOTAL_NUMBER;
//    }
//
//    int getRowNumberTotalPayment() {
//        return ROW_NUMBER_TOTAL_PAYMENT;
//    }
//
//    @Override
//    int getRowCount() {
//        return ROW_COUNT;
//    }

//    private boolean filtered() {
//        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
//    }

    final void bindTotalPayment(@DrawableRes int icon, @StringRes int title, @NonNull List<Entity> items, @NonNull ItemViewHolder holder) {
        @NonNull final String secondLine;
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Payable item : items) {
            totalPayment = totalPayment.add(item.getPaymentData().getPayment());
        }
        if (callbacks.filtered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal filteredPayment = BigDecimal.ZERO;
            for (Entity item : items) {
                if (callbacks.isIncluded(item)) filteredPayment = filteredPayment.add(item.getPaymentData().getPayment());
            }
            secondLine = holder.getPaymentPercentage(filteredPayment, totalPayment);
        } else {
            secondLine = holder.getPaymentText(totalPayment);
        }
        holder.setupTotals(R.drawable.ic_dollar_black_24dp, R.string.total_payment, secondLine);
    }

//    final boolean isIncluded(@NonNull PaymentData paymentData) {
//        if (paymentData.getClaimed() == null) {
//            return callbacks.includeUnclaimed();
//        } else if (paymentData.getPaid() == null) {
//            return callbacks.includeClaimed();
//        } else {
//            return callbacks.includePaid();
//        }
//    }

    @Override
    public boolean bindViewHolder(@NonNull List<Entity> items, @NonNull ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberTotalNumber()) {
            bindTotalNumber(R.drawable.ic_list_black_24dp, getTitle(), items, holder);
            return true;
        } else if (position == callbacks.getRowNumberTotalPayment()) {
            bindTotalPayment(R.drawable.ic_dollar_black_24dp, R.string.total_payment, items, holder);
            return true;
        } else return false;
    }

    public interface Callbacks<T> {
        int getRowNumberTotalPayment();
        int getRowNumberTotalNumber();
        boolean filtered();
        boolean isIncluded(@NonNull T item);
    }

}
