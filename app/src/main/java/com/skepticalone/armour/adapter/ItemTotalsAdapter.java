package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

import java.util.List;

public abstract class ItemTotalsAdapter<FinalItem> extends ContextAdapter<ItemViewHolder> implements Observer<List<FinalItem>> {

    @StringRes
    private final int totalItemsTitle;

    @Nullable
    private List<FinalItem> mItems;

    ItemTotalsAdapter(@NonNull Context context, @StringRes int totalItemsTitle) {
        super(context);
        this.totalItemsTitle = totalItemsTitle;
    }

    @Override
    @CallSuper
    public void onChanged(@Nullable List<FinalItem> newItems) {
        if (mItems != null && newItems != null) {
            notifyItemRangeChanged(0, getFixedRowCount());
        } else if (newItems != null) {
            notifyItemRangeInserted(0, getFixedRowCount());
        } else if (mItems != null) {
            notifyItemRangeRemoved(0, getFixedRowCount());
        }
        mItems = newItems;
    }

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : getFixedRowCount();
    }

    abstract int getFixedRowCount();

    @StringRes
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
        ItemViewHolder viewHolder = new ItemViewHolder(parent);
        viewHolder.setupPlain();
        viewHolder.hideSecondaryIcon();
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        onBindViewHolder(mItems, position, holder);
    }

    abstract void onBindViewHolder(@NonNull List<FinalItem> items, int position, @NonNull ItemViewHolder holder);

}
