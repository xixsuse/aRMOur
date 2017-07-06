package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.model.Item;

import java.util.List;

public abstract class ItemListAdapter<ItemType extends Item> extends RecyclerView.Adapter<ItemViewHolder> {

    @NonNull
    private final Callbacks mCallbacks;
    @Nullable
    private List<? extends ItemType> mItems;

    ItemListAdapter(@NonNull Callbacks callbacks) {
        super();
        setHasStableIds(true);
        mCallbacks = callbacks;
    }

    @Override
    public final long getItemId(int position) {
        //noinspection ConstantConditions
        return mItems.get(position).getId();
    }

    public final void setItems(final List<? extends ItemType> items) {
        if (mItems == null) {
            notifyItemRangeInserted(0, items.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItems.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ItemListAdapter.this.areItemsTheSame(mItems.get(oldItemPosition), items.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ItemListAdapter.this.areContentsTheSame(mItems.get(oldItemPosition), items.get(newItemPosition));
                }

            });
            result.dispatchUpdatesTo(this);
        }
        mItems = items;
    }

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ItemViewHolder viewHolder = new ItemViewHolder(parent);
        viewHolder.primaryIcon.setVisibility(View.GONE);
        viewHolder.switchControl.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClick(viewHolder.getItemId());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCallbacks.onLongClick(viewHolder.getItemId());
                return true;
            }
        });
        return viewHolder;
    }

    abstract boolean areItemsTheSame(@NonNull ItemType item1, @NonNull ItemType item2);

    abstract boolean areContentsTheSame(@NonNull ItemType item1, @NonNull ItemType item2);

    abstract void bindViewHolder(@NonNull ItemType item, ItemViewHolder holder);

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        bindViewHolder(mItems.get(position), holder);
    }

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public interface Callbacks {
        void onClick(long itemId);

        void onLongClick(long itemId);
    }
}
