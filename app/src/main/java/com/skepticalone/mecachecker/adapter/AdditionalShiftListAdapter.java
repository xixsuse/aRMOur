package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.AdditionalShift;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public final class AdditionalShiftListAdapter extends ItemListAdapter<AdditionalShift> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull AdditionalShift shift1, @NonNull AdditionalShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalPaymentData(shift1.getPaymentData(), shift2.getPaymentData()) &&
                shift1.getShift().equals(shift2.getShift());
    }

    @Override
    void bindViewHolder(@NonNull AdditionalShift shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(R.drawable.ic_long_day_black_24dp);
        holder.secondaryIcon.setImageResource(R.drawable.ic_cancel_red_24dp);
        holder.setText(DateTimeUtils.getFullDateString(shift.getShift().getStart().toLocalDate()), holder.getText(R.string.currency_format, shift.getPaymentData().getPayment()), shift.getComment());
    }

}
