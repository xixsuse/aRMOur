package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

import java.math.BigDecimal;


abstract class ObservableAdapter<Data> extends RecyclerView.Adapter<ItemViewHolder> implements Observer<Data> {

    @NonNull
    private final Context mContext;

    @Nullable
    private Data mData;

    ObservableAdapter(@NonNull Context context) {
        super();
        mContext = context;
    }

    @NonNull
    final Context getContext() {
        return mContext;
    }

    @Override
    public final void onChanged(@Nullable Data data) {
        if (mData == null && data == null) {
            return;
        } else if (mData == null) {
            notifyItemRangeInserted(0, getItemCount(data));
        } else if (data == null) {
            notifyItemRangeRemoved(0, getItemCount(mData));
        } else {
            onChanged(mData, data);
        }
        mData = data;
    }

    abstract void onChanged(@NonNull Data oldData, @NonNull Data newData);

    @Override
    public final int getItemCount() {
        return mData == null ? 0 : getItemCount(mData);
    }

    abstract int getItemCount(@NonNull Data data);

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @Override
    public final long getItemId(int position) {
        //noinspection ConstantConditions
        return getItemId(position, mData);
    }

    long getItemId(int position, @NonNull Data data) {
        return super.getItemId(position);
    }

    @Override
    public final void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        onBindViewHolder(mData, position, holder);
    }

    abstract void onBindViewHolder(@NonNull Data data, int position, @NonNull ItemViewHolder holder);

    @NonNull
    final String getPaymentString(@NonNull BigDecimal payment) {
        return mContext.getString(R.string.payment_format, payment);
    }

}
