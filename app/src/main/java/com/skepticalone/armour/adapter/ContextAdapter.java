package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

import java.math.BigDecimal;


abstract class ContextAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    @NonNull
    private final Context mContext;

    ContextAdapter(@NonNull Context context) {
        super();
        mContext = context;
    }

    @NonNull
    final String getPaymentString(@NonNull BigDecimal payment) {
        return mContext.getString(R.string.payment_format, payment);
    }

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @NonNull
    Context getContext() {
        return mContext;
    }

}
