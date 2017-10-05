package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Payable;

import java.math.BigDecimal;
import java.util.List;

public abstract class PayableTotalsAdapter<Entity extends Payable> extends ItemTotalsAdapter<Entity> {

    @NonNull
    private final Callbacks callbacks;

    PayableTotalsAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @NonNull
    final String getTotalPayment(@NonNull List<Entity> items, @NonNull ItemViewHolder holder) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Entity item : items) {
            totalPayment = totalPayment.add(item.getTotalPayment());
        }
        if (isFiltered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal filteredPayment = BigDecimal.ZERO;
            for (Entity item : items) {
                if (isIncluded(item)) filteredPayment = filteredPayment.add(item.getTotalPayment());
            }
            return holder.getPaymentPercentage(filteredPayment, totalPayment);
        } else {
            return holder.getPaymentString(totalPayment);
        }
    }

    @Override
    public final boolean isFiltered() {
        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
    }

    @Override
    public final boolean isIncluded(@NonNull Entity item) {
        return item.getPaymentData().getClaimed() == null ? callbacks.includeUnclaimed() : item.getPaymentData().getPaid() == null ? callbacks.includeClaimed() : callbacks.includePaid();
    }

    public interface Callbacks {
        boolean includeUnclaimed();
        boolean includeClaimed();
        boolean includePaid();
    }

}
