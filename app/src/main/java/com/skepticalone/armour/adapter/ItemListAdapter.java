package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;

import java.util.List;

public abstract class ItemListAdapter<Entity extends Item> extends ContextAdapter implements ListUpdateCallback, Observer<List<Entity>> {

    @NonNull
    private final Callbacks mCallbacks;

    @Nullable
    private List<Entity> mItems;

    ItemListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        setHasStableIds(true);
        mCallbacks = callbacks;
    }

    @Override
    public final long getItemId(int position) {
        //noinspection ConstantConditions
        return mItems.get(position).getId();
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
        final ItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.setupPlain();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClick(viewHolder);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mCallbacks.onLongClick(viewHolder);
            }
        });
        return viewHolder;
    }

    abstract boolean areContentsTheSame(@NonNull Entity item1, @NonNull Entity item2);

    @DrawableRes
    abstract int getPrimaryIcon(@NonNull Entity item);

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull Entity item);

    @NonNull
    abstract String getFirstLine(@NonNull Entity item);

    @NonNull
    abstract String getSecondLine(@NonNull Entity item);

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        Entity item = mItems.get(position);
        holder.setPrimaryIcon(mCallbacks.showSelectedIcon(position) ? R.drawable.ic_check_circle_24dp : getPrimaryIcon(item));
        holder.setSecondaryIcon(getSecondaryIcon(item));
        holder.setText(getFirstLine(item), getSecondLine(item), item.getComment());
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
        void onClick(@NonNull RecyclerView.ViewHolder viewHolder);

        boolean onLongClick(@NonNull RecyclerView.ViewHolder viewHolder);
        void scrollToPosition(int position);

        boolean showSelectedIcon(int position);
    }

}
