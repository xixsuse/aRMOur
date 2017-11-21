package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;

import java.util.List;

public abstract class ItemListAdapter<FinalItem extends Item> extends ContextAdapter<ItemViewHolder> implements Observer<List<FinalItem>> {

    @NonNull
    private final Callbacks mCallbacks;

    @Nullable
    private List<FinalItem> mItems;

    ItemListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        setHasStableIds(true);
        mCallbacks = callbacks;
    }

    @Override
    public final int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public final long getItemId(int position) {
        //noinspection ConstantConditions
        return mItems.get(position).getId();
    }

    abstract boolean areContentsTheSame(@NonNull FinalItem item1, @NonNull FinalItem item2);

    @Override
    public final void onChanged(@Nullable final List<FinalItem> newItems) {
        if (mItems != null && newItems != null) {
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItems.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mItems.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ItemListAdapter.this.areContentsTheSame(mItems.get(oldItemPosition), newItems.get(newItemPosition));
                }
            }).dispatchUpdatesTo(this);
        } else if (newItems != null) {
            notifyItemRangeInserted(0, newItems.size());
        } else if (mItems != null) {
            notifyItemRangeRemoved(0, mItems.size());
        }
        mItems = newItems;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final ItemViewHolder viewHolder = new ItemViewHolder(parent);
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

    @DrawableRes
    abstract int getPrimaryIcon(@NonNull FinalItem item);

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull FinalItem item);

    @NonNull
    abstract String getFirstLine(@NonNull FinalItem item);

    @NonNull
    abstract String getSecondLine(@NonNull FinalItem item);

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        FinalItem item = mItems.get(position);
        holder.setPrimaryIcon(mCallbacks.showSelectedIcon(position) ? R.drawable.ic_check_circle_red_24dp : getPrimaryIcon(item));
        holder.setSecondaryIcon(getSecondaryIcon(item));
        holder.setText(getFirstLine(item), getSecondLine(item), item.getComment());
    }

    public interface Callbacks {
        void onClick(@NonNull RecyclerView.ViewHolder viewHolder);
        boolean onLongClick(@NonNull RecyclerView.ViewHolder viewHolder);
        boolean showSelectedIcon(int position);
    }

}
