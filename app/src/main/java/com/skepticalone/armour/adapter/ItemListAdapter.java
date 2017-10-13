package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ItemListAdapter<Entity extends Item> extends RecyclerView.Adapter<ItemViewHolder> implements ListUpdateCallback, Observer<List<Entity>> {

    @NonNull
    private final Callbacks mCallbacks;
    @Nullable
    private List<Entity> mItems;

    @Nullable
    private Set<Long> mSelectedIds;
//
//    @NonNull
//    private final Observer<Set<Long>> mSelectedIdsObserver = new Observer<Set<Long>>() {
//        @Override
//        public void onChanged(@Nullable Set<Long> selectedIds) {
//            if (mItems == null && items == null) {
//                return;
//            }
//        }
//    };

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

    public final void notifyIdsChanged(@Nullable final Set<Long> selectedIds) {
        if (mItems != null && (mSelectedIds != null || selectedIds != null)) {
            Set<Long> idsToNotify = new HashSet<>();
            if (mSelectedIds != null) idsToNotify.addAll(mSelectedIds);
            if (selectedIds != null) idsToNotify.addAll(selectedIds);
            if (mSelectedIds != null && selectedIds != null) {
                Set<Long> intersection = new HashSet<>(mSelectedIds);
                intersection.retainAll(selectedIds);
                idsToNotify.removeAll(intersection);
            }
            for (int position = 0; position < mItems.size(); position++) {
                if (idsToNotify.contains(getItemId(position))) {
                    notifyItemChanged(position);
                }
            }
        }
        mSelectedIds = selectedIds;
    }

    @Override
    public final void onChanged(@Nullable final List<Entity> items) {
        if (mItems == null && items == null) {
            return;
        } else if (mItems == null) {
            notifyItemRangeInserted(0, items.size());
        } else if (items == null) {
            notifyItemRangeRemoved(0, mItems.size());
        } else {
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
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
                    return mItems.get(oldItemPosition).getId() == items.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ItemListAdapter.this.areContentsTheSame(mItems.get(oldItemPosition), items.get(newItemPosition));
                }

            }).dispatchUpdatesTo((ListUpdateCallback) this);
        }
        mItems = items;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ItemViewHolder viewHolder = new ItemViewHolder(parent);
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
                return mCallbacks.onLongClick(viewHolder.getItemId());
            }
        });
        return viewHolder;
    }

    abstract boolean areContentsTheSame(@NonNull Entity item1, @NonNull Entity item2);

    abstract void bindViewHolder(@NonNull Entity item, ItemViewHolder holder, boolean selected);

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        Entity item = mItems.get(position);
        bindViewHolder(item, holder, mSelectedIds != null && mSelectedIds.contains(item.getId()));
    }

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public final void onInserted(int position, int count) {
        notifyItemRangeInserted(position, count);
        mCallbacks.scrollToPosition(position + count - 1);
    }

    @Override
    public final void onRemoved(int position, int count) {
        notifyItemRangeRemoved(position, count);
    }

    @Override
    public final void onMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public final void onChanged(int position, int count, Object payload) {
        notifyItemRangeChanged(position, count, payload);
    }

    public interface Callbacks {
        void onClick(long itemId);
        boolean onLongClick(long itemId);
        void scrollToPosition(int position);
    }


}
