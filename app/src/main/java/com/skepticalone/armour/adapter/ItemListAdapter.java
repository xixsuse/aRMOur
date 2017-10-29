package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;

import java.util.List;

public abstract class ItemListAdapter<FinalItem extends Item> extends GuidedObservableAdapter<List<FinalItem>> implements ListUpdateCallback {

    @NonNull
    private final Callbacks mCallbacks;

    ItemListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        setHasStableIds(true);
        mCallbacks = callbacks;
    }

    @Override
    final long getItemId(int position, @NonNull List<FinalItem> items) {
        return items.get(position).getId();
    }

    @Override
    final void notifyUpdated(@NonNull final List<FinalItem> oldList, @NonNull final List<FinalItem> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return ItemListAdapter.this.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
            }

        }).dispatchUpdatesTo((ListUpdateCallback) ItemListAdapter.this);
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

    abstract boolean areContentsTheSame(@NonNull FinalItem item1, @NonNull FinalItem item2);

    @DrawableRes
    abstract int getPrimaryIcon(@NonNull FinalItem item);

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull FinalItem item);

    @NonNull
    abstract String getFirstLine(@NonNull FinalItem item);

    @NonNull
    abstract String getSecondLine(@NonNull FinalItem item);

    @Override
    final void onBindViewHolder(@NonNull List<FinalItem> items, int position, @NonNull ItemViewHolder holder) {
        FinalItem item = items.get(position);
        holder.setPrimaryIcon(mCallbacks.showSelectedIcon(position) ? R.drawable.ic_check_circle_24dp : getPrimaryIcon(item));
        holder.setSecondaryIcon(getSecondaryIcon(item));
        holder.setText(getFirstLine(item), getSecondLine(item), item.getComment());
    }

    @Override
    final int getRowCount(@NonNull List<FinalItem> items) {
        return items.size();
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
