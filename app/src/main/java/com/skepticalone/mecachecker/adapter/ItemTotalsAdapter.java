package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Item;

import java.util.List;

public abstract class ItemTotalsAdapter<Entity extends Item> extends RecyclerView.Adapter<ItemViewHolder> implements CompoundButton.OnCheckedChangeListener {

    abstract boolean isFiltered();

    abstract boolean isIncluded(@NonNull Entity item);

    @Nullable
    private List<Entity> mItems;

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

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ItemViewHolder viewHolder = new ItemViewHolder(parent);
        viewHolder.switchControl.setVisibility(View.GONE);
        viewHolder.secondaryIcon.setVisibility(View.GONE);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        if (!bindViewHolder(mItems, holder, position)){
            throw new IllegalStateException();
        }
    }

    @NonNull
    final String getTotalNumber(@NonNull List<Entity> items, @NonNull ItemViewHolder holder) {
        int totalCount = items.size();
        if (isFiltered() && totalCount > 0) {
            int filteredCount = 0;
            for (Entity item : items) {
                if (isIncluded(item)) filteredCount++;
            }
            return holder.getCountPercentage(filteredCount, totalCount);
        } else {
            return holder.getCount(totalCount);
        }
    }

//    final void bindTotalNumber(@DrawableRes int icon, @StringRes int title, @NonNull List<Entity> items, @NonNull ItemViewHolder holder){
//        @NonNull final String secondLine;
//        int totalCount = items.size();
//        if (isFiltered() && totalCount > 0) {
//            int filteredCount = 0;
//            for (Entity item : items) {
//                if (isIncluded(item)) filteredCount++;
//            }
//            secondLine = holder.getCountPercentage(filteredCount, totalCount);
//        } else {
//            secondLine = holder.getCount(totalCount);
//        }
//        holder.setupTotals(icon, title, secondLine);
//    }

    @CallSuper
    boolean bindViewHolder(@NonNull List<Entity> allItems, @NonNull ItemViewHolder holder, int position){
        if (position == getRowNumberTotalNumber()) {
            holder.setupTotals(R.drawable.ic_list_black_24dp, getTotalNumberTitle(), getTotalNumber(allItems, holder));
            return true;
        } else return false;
    }

    abstract int getRowNumberTotalNumber();

    abstract int getRowCount();

    @StringRes
    abstract int getTotalNumberTitle();

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : getRowCount();
    }

}
