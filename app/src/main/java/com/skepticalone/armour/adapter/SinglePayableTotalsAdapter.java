package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payment;

import java.util.List;

public final class SinglePayableTotalsAdapter<Entity extends Payment> extends PayableTotalsAdapter<Entity> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    @StringRes
    private final int totalItemsTitle;

    public SinglePayableTotalsAdapter(@NonNull Context context, @NonNull Callbacks callbacks, @StringRes int totalItemsTitle) {
        super(context, callbacks);
        this.totalItemsTitle = totalItemsTitle;
    }

    @Override
    final int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    final boolean bindViewHolder(@NonNull List<Entity> allItems, @NonNull ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            holder.setPrimaryIcon(R.drawable.ic_sigma_black_24dp);
            holder.setText(getContext().getString(totalItemsTitle), getTotalNumber(allItems));
            return true;
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            holder.setPrimaryIcon(R.drawable.ic_dollar_black_24dp);
            holder.setText(getContext().getString(R.string.total_payment), getTotalPayment(allItems));
            return true;
        } else return false;
    }

}
