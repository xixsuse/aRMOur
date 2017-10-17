package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

import java.util.List;

public abstract class ItemTotalsAdapter<FinalItem> extends ObservableAdapter<List<FinalItem>> {

    @StringRes
    private final int totalItemsTitle;

    ItemTotalsAdapter(@NonNull Context context, @StringRes int totalItemsTitle) {
        super(context);
        this.totalItemsTitle = totalItemsTitle;
    }

    @Override
    final void notifyUpdated(@NonNull List<FinalItem> oldData, @NonNull List<FinalItem> newData) {
        notifyItemRangeChanged(0, getFixedRowCount());
    }

    @Override
    final int getRowCount(@NonNull List<FinalItem> finalItems) {
        return getFixedRowCount();
    }

    abstract int getFixedRowCount();

    final int getTotalItemsTitle() {
        return totalItemsTitle;
    }

    @NonNull
    final String getCountString(int count) {
        return Integer.toString(count);
    }

    @NonNull
    final String getPercentage(@NonNull String value, int percentage) {
        return getContext().getString(R.string.percentage_format, value, percentage);
    }

    @NonNull
    final String getCountPercentage(int count, int totalCount) {
        return getPercentage(getCountString(count), Math.round(count * 100f / totalCount));
    }

    @NonNull
    final String getDurationPercentage(@NonNull Duration duration, @NonNull Duration totalDuration) {
        return getPercentage(DateTimeUtils.getDurationString(getContext(), duration), Math.round(duration.getSeconds() * 100f / totalDuration.getSeconds()));
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        ItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.setupPlain();
        viewHolder.hideSecondaryIcon();
        return viewHolder;
    }

}
