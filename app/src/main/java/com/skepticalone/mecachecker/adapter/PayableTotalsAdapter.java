package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.Payable;

import java.math.BigDecimal;
import java.util.List;

public abstract class PayableTotalsAdapter<Entity extends Payable> extends ItemTotalsAdapter<Entity> {

    @NonNull
    private final Callbacks callbacks;

    PayableTotalsAdapter(@NonNull Callbacks callbacks) {
        super();
        this.callbacks = callbacks;
    }

    @NonNull
    final String getTotalPayment(@NonNull List<Entity> items, @NonNull ItemViewHolder holder) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Entity item : items) {
            totalPayment = totalPayment.add(item.getPayment());
        }
        if (isFiltered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal filteredPayment = BigDecimal.ZERO;
            for (Entity item : items) {
                if (isIncluded(item)) filteredPayment = filteredPayment.add(item.getPayment());
            }
            return holder.getPaymentPercentage(filteredPayment, totalPayment);
        } else {
            return holder.getPaymentText(totalPayment);
        }
    }

    @Override
    final boolean isFiltered() {
        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
    }

    @Override
    final boolean isIncluded(@NonNull Entity item) {
        return item.getPaymentData().getClaimed() == null ? callbacks.includeUnclaimed() : item.getPaymentData().getPaid() == null ? callbacks.includeClaimed() : callbacks.includePaid();
    }

    public interface Callbacks {
        boolean includeUnclaimed();
        boolean includeClaimed();
        boolean includePaid();
    }

}
