package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Payable;

import java.util.List;

public final class SinglePayableTotalsAdapter<Entity extends Payable> extends PayableTotalsAdapter<Entity> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    @StringRes
    private final int totalItemsTitle;

    public SinglePayableTotalsAdapter(@NonNull Callbacks callbacks, @StringRes int totalItemsTitle) {
        super(callbacks);
        this.totalItemsTitle = totalItemsTitle;
    }

    @Override
    final int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    final boolean bindViewHolder(@NonNull List<Entity> allItems, @NonNull ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            holder.setupTotals(R.drawable.ic_list_black_24dp, totalItemsTitle, getTotalNumber(allItems, holder), null);
            return true;
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            holder.setupTotals(R.drawable.ic_dollar_black_24dp, R.string.total_payment, getTotalPayment(allItems, holder), null);
            return true;
        } else return false;
    }

}
