package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public final class CrossCoverListAdapter extends ItemListAdapter<CrossCoverEntity> {

    public CrossCoverListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.primaryIcon.setVisibility(View.GONE);
        return viewHolder;
    }

    @Override
    boolean areContentsTheSame(@NonNull CrossCoverEntity crossCover1, @NonNull CrossCoverEntity crossCover2) {
        return super.areContentsTheSame(crossCover1, crossCover2) &&
                Comparators.equalPaymentData(crossCover1.getPaymentData(), crossCover2.getPaymentData()) &&
                Comparators.equalLocalDates(crossCover1.getDate(), crossCover2.getDate());
    }

    @Override
    void bindViewHolder(@NonNull CrossCoverEntity crossCover, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(crossCover.getPaymentData().getIcon());
        holder.setText(DateTimeUtils.getFullDateString(crossCover.getDate()), holder.getCurrencyText(crossCover.getPaymentData().getPayment()), crossCover.getComment());
    }

}
