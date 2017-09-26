package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class CrossCoverListAdapter extends ItemListAdapter<CrossCover> {

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
    boolean areContentsTheSame(@NonNull CrossCover crossCover1, @NonNull CrossCover crossCover2) {
        return crossCover1.getDate().isEqual(crossCover2.getDate()) &&
                crossCover1.getTotalPayment().equals(crossCover2.getTotalPayment()) &&
                Comparators.equalStrings(crossCover1.getComment(), crossCover2.getComment()) &&
                crossCover1.getPaymentData().getIcon() == crossCover2.getPaymentData().getIcon();
    }

    @Override
    void bindViewHolder(@NonNull CrossCover crossCover, ItemViewHolder holder) {
        holder.setText(DateTimeUtils.getFullDateString(crossCover.getDate()), holder.getPaymentString(crossCover.getTotalPayment()), crossCover.getComment());
        holder.secondaryIcon.setImageResource(crossCover.getPaymentData().getIcon());
    }

}
