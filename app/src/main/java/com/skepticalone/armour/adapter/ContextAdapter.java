package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

abstract class ContextAdapter<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    @NonNull
    private final Context mContext;

    ContextAdapter(@NonNull Context context) {
        super();
        mContext = context;
    }

    @NonNull
    final Context getContext() {
        return mContext;
    }

}
