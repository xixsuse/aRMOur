package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.model.Item;

public abstract class ItemDetailAdapter<ItemType extends Item> extends RecyclerView.Adapter<ItemViewHolder> {

    @Nullable
    private ItemType mItem;

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    public final int getItemCount() {
        return mItem == null ? 0 : getRowCount(mItem);
    }

    abstract int getRowCount(@NonNull ItemType item);

    abstract void onItemUpdated(@NonNull ItemType oldItem, @NonNull ItemType newItem);

    abstract void bindViewHolder(@NonNull ItemType item, ItemViewHolder holder, int position);

    public final void setItem(@NonNull ItemType item) {
        if (mItem == null) {
            mItem = item;
            notifyItemRangeInserted(0, getRowCount(mItem));
        } else {
            onItemUpdated(mItem, item);
            mItem = item;
        }
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        bindViewHolder(mItem, holder, position);
    }
}
