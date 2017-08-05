package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
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

    @StringRes
    abstract int getTotalNumberTitle();

    @StringRes
    int getTotalPaymentTitle() {
        return R.string.total_payment;
    }

    abstract int getRowNumberTotalNumber();

    abstract int getRowNumberTotalPayment();

    final void bindTotalPayment(@DrawableRes int icon, @StringRes int title, @NonNull List<Entity> items, @NonNull ItemViewHolder holder) {
        @NonNull final String secondLine;
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Entity item : items) {
            totalPayment = totalPayment.add(item.getPayment());
        }
        if (isFiltered() && totalPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal filteredPayment = BigDecimal.ZERO;
            for (Entity item : items) {
                if (isIncluded(item)) filteredPayment = filteredPayment.add(item.getPayment());
            }
            secondLine = holder.getPaymentPercentage(filteredPayment, totalPayment);
        } else {
            secondLine = holder.getPaymentText(totalPayment);
        }
        holder.setupTotals(icon, title, secondLine);
    }

    @Override
    final boolean isFiltered() {
        return !callbacks.includeUnclaimed() || !callbacks.includeClaimed() || !callbacks.includePaid();
    }

    @Override
    final boolean isIncluded(@NonNull Entity item) {
        if (item.getPaymentData().getClaimed() == null) {
            return callbacks.includeUnclaimed();
        } else if (item.getPaymentData().getPaid() == null) {
            return callbacks.includeClaimed();
        } else {
            return callbacks.includePaid();
        }
    }

    @Override
    @CallSuper
    boolean bindViewHolder(@NonNull List<Entity> allItems, @NonNull ItemViewHolder holder, int position) {
        if (position == getRowNumberTotalNumber()) {
            bindTotalNumber(R.drawable.ic_list_black_24dp, getTotalNumberTitle(), allItems, holder);
            return true;
        } else if (position == getRowNumberTotalPayment()) {
            bindTotalPayment(R.drawable.ic_dollar_black_24dp, getTotalPaymentTitle(), allItems, holder);
            return true;
        } else return false;
    }

    public interface Callbacks {
        boolean includeUnclaimed();
        boolean includeClaimed();
        boolean includePaid();
    }

}
