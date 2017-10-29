package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

abstract class GuidedObservableAdapter<Data> extends ObservableAdapter<Data> {

    @Nullable
    private Data mData;

    GuidedObservableAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    @CallSuper
    public void onChanged(@Nullable Data newData) {
        onChanged(mData, newData);
        mData = newData;
    }

    @CallSuper
    void onChanged(@Nullable Data oldData, @Nullable Data newData) {
        if (oldData != null && newData != null) {
            notifyUpdated(oldData, newData);
        } else if (newData != null) {
            notifyItemRangeInserted(0, getRowCount(newData));
        } else if (oldData != null) {
            notifyItemRangeRemoved(0, getRowCount(oldData));
        }
    }

    abstract void notifyUpdated(@NonNull Data oldData, @NonNull Data newData);

    @Override
    public final int getItemCount() {
        return mData == null ? 0 : getRowCount(mData);
    }

    abstract int getRowCount(@NonNull Data data);

    @Override
    public final long getItemId(int position) {
        //noinspection ConstantConditions
        return getItemId(position, mData);
    }

    long getItemId(int position, @NonNull Data data) {
        return RecyclerView.NO_ID;
    }

    @Override
    public final void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        onBindViewHolder(mData, position, holder);
    }

    abstract void onBindViewHolder(@NonNull Data data, int position, @NonNull ItemViewHolder holder);

}
