package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import java.util.List;

interface TotalsAdapterCallbacks<Entity> {
    boolean isFiltered();
    boolean isIncluded(@NonNull Entity item);
    @NonNull
    String getTotalNumber(@NonNull List<Entity> items, @NonNull ItemViewHolder holder);
}
