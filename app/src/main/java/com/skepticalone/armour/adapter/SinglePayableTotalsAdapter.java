package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payment;

import java.util.List;

public final class SinglePayableTotalsAdapter<FinalItem extends Payment> extends PayableTotalsAdapter<FinalItem> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    public SinglePayableTotalsAdapter(@NonNull Context context, int totalItemsTitle, @NonNull Callbacks callbacks) {
        super(context, totalItemsTitle, callbacks);
    }

    @Override
    int getFixedItemCount() {
        return ROW_COUNT;
    }

    @Override
    void onBindViewHolder(@NonNull List<FinalItem> allItems, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_TOTAL_NUMBER) {
            holder.setPrimaryIcon(R.drawable.ic_sigma_black_24dp);
            holder.setText(getContext().getString(getTotalItemsTitle()), getTotalNumber(allItems));
        } else if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            holder.setPrimaryIcon(R.drawable.ic_dollar_black_24dp);
            holder.setText(getContext().getString(R.string.total_payment), getTotalPayment(allItems));
        }
    }

}
