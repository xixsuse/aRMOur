package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public class CrossCoverListAdapter extends ItemListAdapter<CrossCover> {

    public CrossCoverListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull CrossCover crossCover1, @NonNull CrossCover crossCover2) {
        return
                Comparators.equalLocalDates(crossCover1.getDate(), crossCover2.getDate()) &&
                        Comparators.equalPaymentData(crossCover1.getPaymentData(), crossCover2.getPaymentData());
    }

    @Override
    void bindViewHolder(@NonNull CrossCover crossCover, ItemViewHolder holder) {
        holder.setText(DateTimeUtils.getFullDateString(crossCover.getDate()), crossCover.getPaymentData().getComment());
        holder.secondaryIcon.setImageResource(crossCover.getPaymentData().getIcon());
    }
}
