package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public abstract class PayableTotalsAdapter<FinalItem extends Payment> extends FilteredItemTotalsAdapter<FinalItem> {

    @NonNull
    private final Callbacks callbacks;

    PayableTotalsAdapter(@NonNull Context context, int totalItemsTitle, @NonNull Callbacks callbacks) {
        super(context, totalItemsTitle);
        this.callbacks = callbacks;
    }

    @NonNull
    private String getPaymentPercentage(@NonNull BigDecimal payment, @NonNull BigDecimal totalPayment) {
        return getPercentage(getPaymentString(payment), payment.movePointRight(2).divide(totalPayment, BigDecimal.ROUND_HALF_UP).intValue());
    }

    @NonNull
    final String getTotalPayment(@NonNull List<FinalItem> items) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (FinalItem item : items) {
            totalPayment = totalPayment.add(item.getTotalPayment());
        }
        if (isFiltered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal filteredPayment = BigDecimal.ZERO;
            for (FinalItem item : items) {
                if (isIncluded(item)) filteredPayment = filteredPayment.add(item.getTotalPayment());
            }
            return getPaymentPercentage(filteredPayment, totalPayment);
        } else {
            return getPaymentString(totalPayment);
        }
    }

    @Override
    final boolean isFiltered() {
        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
    }

    @Override
    final boolean isIncluded(@NonNull FinalItem item) {
        return item.getPaymentData().getClaimed() == null ? callbacks.includeUnclaimed() : item.getPaymentData().getPaid() == null ? callbacks.includeClaimed() : callbacks.includePaid();
    }

    public interface Callbacks {
        boolean includeUnclaimed();
        boolean includeClaimed();
        boolean includePaid();
    }

}
