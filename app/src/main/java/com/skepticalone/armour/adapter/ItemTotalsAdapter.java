package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

import java.util.List;

public abstract class ItemTotalsAdapter<Entity> extends ContextAdapter implements CompoundButton.OnCheckedChangeListener, TotalsAdapterCallbacks<Entity> {

    @Nullable
    private List<Entity> mItems;

    ItemTotalsAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    private String getCountString(int count) {
        return Integer.toString(count);
    }

    @NonNull
    final String getPercentage(@NonNull String value, int percentage) {
        return getContext().getString(R.string.percentage_format, value, percentage);
    }

    @NonNull
    private String getCountPercentage(int count, int totalCount) {
        return getPercentage(getCountString(count), Math.round(count * 100f / totalCount));
    }


    @NonNull
    final String getDurationPercentage(@NonNull Duration duration, @NonNull Duration totalDuration) {
        return getPercentage(DateTimeUtils.getDurationString(getContext(), duration), Math.round(duration.getSeconds() * 100f / totalDuration.getSeconds()));
    }

    public final void setItems(final @Nullable List<Entity> items) {
        if (mItems == null && items == null) {
            return;
        } else if (mItems == null) {
            notifyItemRangeInserted(0, getRowCount());
        } else if (items == null) {
            notifyItemRangeRemoved(0, getRowCount());
        } else {
            notifyItemRangeChanged(0, getRowCount());
        }
        mItems = items;
    }

    @Override
    public final void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (mItems != null) {
            notifyItemRangeChanged(0, getRowCount());
        }
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        ItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.setupPlain();
        viewHolder.hideSecondaryIcon();
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        if (!bindViewHolder(mItems, holder, position)) {
            throw new IllegalStateException();
        }
    }

    @NonNull
    @Override
    public final String getTotalNumber(@NonNull List<Entity> items) {
        int totalCount = items.size();
        if (isFiltered() && totalCount > 0) {
            int filteredCount = 0;
            for (Entity item : items) {
                if (isIncluded(item)) filteredCount++;
            }
            return getCountPercentage(filteredCount, totalCount);
        } else {
            return getCountString(totalCount);
        }
    }

    abstract boolean bindViewHolder(@NonNull List<Entity> allItems, @NonNull ItemViewHolder holder, int position);

    abstract int getRowCount();

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : getRowCount();
    }

}
