package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class CrossCoverListAdapter extends ItemListAdapter<CrossCover> {

    public CrossCoverListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull CrossCover crossCover1, @NonNull CrossCover crossCover2) {
        return crossCover1.getDate().isEqual(crossCover2.getDate()) &&
                crossCover1.getTotalPayment().equals(crossCover2.getTotalPayment()) &&
                Comparators.equalStrings(crossCover1.getComment(), crossCover2.getComment()) &&
                crossCover1.getPaymentData().getIcon() == crossCover2.getPaymentData().getIcon();
    }

    @Override
    int getPrimaryIcon(@NonNull CrossCover crossCover) {
        return R.drawable.ic_cross_cover_black_24dp;
    }

    @Override
    int getSecondaryIcon(@NonNull CrossCover crossCover) {
        return crossCover.getPaymentData().getIcon();
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull CrossCover crossCover) {
        return DateTimeUtils.getFullDateString(crossCover.getDate());
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull CrossCover crossCover) {
        return getContext().getString(R.string.payment_format, crossCover.getTotalPayment());
    }

}
