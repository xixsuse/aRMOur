package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.CrossCover;

import java.util.ArrayList;
import java.util.List;

public final class CrossCoverDetailAdapter extends ItemDetailAdapter<CrossCover> {

    @NonNull
    private final Callbacks callbacks;

    public CrossCoverDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    List<ItemViewHolder.Binder> getNewList(@NonNull CrossCover crossCover) {
        List<ItemViewHolder.Binder> list = new ArrayList<>(5);
        list.add(new DateBinder(callbacks, crossCover.getDate()));
        list.add(new PaymentBinder(callbacks, crossCover.getPaymentData().getPayment(), R.drawable.ic_dollar_black_24dp, R.string.payment));
        list.add(new CommentBinder(callbacks, crossCover.getComment()));
        list.add(new ClaimedSwitchBinder(callbacks, crossCover.getPaymentData()));
        list.add(new PaidSwitchBinder(callbacks, crossCover.getPaymentData()));
        return list;
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateBinder.Callbacks, PaymentBinder.Callbacks, ClaimedSwitchBinder.Callbacks, PaidSwitchBinder.Callbacks {
    }

}
