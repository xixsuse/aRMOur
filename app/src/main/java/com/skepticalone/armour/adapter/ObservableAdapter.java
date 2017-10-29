package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

import java.math.BigDecimal;


public abstract class ObservableAdapter<Data> extends RecyclerView.Adapter<ItemViewHolder> implements Observer<Data> {

    @NonNull
    private final Context mContext;


    ObservableAdapter(@NonNull Context context) {
        super();
        mContext = context;
    }

    @NonNull
    final Context getContext() {
        return mContext;
    }

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @NonNull
    final String getPaymentString(@NonNull BigDecimal payment) {
        return mContext.getString(R.string.payment_format, payment);
    }

}
