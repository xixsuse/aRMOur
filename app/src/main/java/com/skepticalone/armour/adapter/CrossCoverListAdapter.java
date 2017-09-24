package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class CrossCoverListAdapter extends ItemListAdapter<RawCrossCoverEntity> {

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
    boolean areContentsTheSame(@NonNull RawCrossCoverEntity crossCover1, @NonNull RawCrossCoverEntity crossCover2) {
        return super.areContentsTheSame(crossCover1, crossCover2) &&
                Comparators.equalPaymentData(crossCover1.getPaymentData(), crossCover2.getPaymentData()) &&
                Comparators.equalLocalDates(crossCover1.getDate(), crossCover2.getDate());
    }

    @Override
    void bindViewHolder(@NonNull RawCrossCoverEntity crossCover, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(crossCover.getPaymentData().getIcon());
        holder.setText(DateTimeUtils.getFullDateString(crossCover.getDate()), holder.getPaymentString(crossCover.getTotalPayment()), crossCover.getComment());
    }

}
