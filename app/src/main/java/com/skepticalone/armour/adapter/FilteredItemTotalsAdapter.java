package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.CompoundButton;

import java.util.List;

public abstract class FilteredItemTotalsAdapter<FinalItem> extends ItemTotalsAdapter<FinalItem> implements CompoundButton.OnCheckedChangeListener {

    FilteredItemTotalsAdapter(@NonNull Context context, @StringRes int totalItemsTitle) {
        super(context, totalItemsTitle);
    }

    abstract boolean isFiltered();

    abstract boolean isIncluded(@NonNull FinalItem item);

    @Override
    public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        notifyItemRangeChanged(0, getFixedItemCount());
    }

    @NonNull
    final String getTotalNumber(@NonNull List<FinalItem> items) {
        int totalCount = items.size();
        if (isFiltered() && totalCount > 0) {
            int filteredCount = 0;
            for (FinalItem item : items) {
                if (isIncluded(item)) filteredCount++;
            }
            return getCountPercentage(filteredCount, totalCount);
        } else {
            return getCountString(totalCount);
        }
    }

}
