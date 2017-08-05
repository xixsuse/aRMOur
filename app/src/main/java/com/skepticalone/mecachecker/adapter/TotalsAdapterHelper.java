package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import java.util.List;

interface TotalsAdapterHelper<Entity> {
    boolean bindViewHolder(@NonNull List<Entity> filteredItems, @NonNull ItemViewHolder holder, int position);
}
