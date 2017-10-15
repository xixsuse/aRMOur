package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.List;

interface TotalsAdapterCallbacks<Entity> {
    boolean isFiltered();
    boolean isIncluded(@NonNull Entity item);
    @NonNull
    String getTotalNumber(@NonNull List<Entity> items);

    @StringRes
    int getTotalItemsTitle();
}
