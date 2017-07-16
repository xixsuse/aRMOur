package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.util.Comparators;

public abstract class ItemDetailAdapter<ItemType extends Item> extends RecyclerView.Adapter<ItemViewHolder> {

    private final Callbacks<ItemType> callbacks;
    @Nullable
    private ItemType mItem;

    ItemDetailAdapter(Callbacks<ItemType> callbacks) {
        super();
        this.callbacks = callbacks;
    }

    @CallSuper
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    public final int getItemCount() {
        return mItem == null ? 0 : callbacks.getRowCount(mItem);
    }

    @CallSuper
    void onItemUpdated(@NonNull ItemType oldItem, @NonNull ItemType newItem) {
        if (!Comparators.equalStrings(oldItem.getComment(), newItem.getComment())) {
            notifyItemChanged(callbacks.getRowNumberComment());
        }
    }

    @CallSuper
    boolean bindViewHolder(@NonNull final ItemType item, ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberComment()) {
            holder.setupPlain(R.drawable.ic_comment_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeComment(item.getId(), item.getComment());
                }
            });
            holder.setText(holder.getText(R.string.comment), item.getComment());
            return true;
        } else return false;
    }

    public final void setItem(@NonNull ItemType item) {
        if (mItem == null) {
            mItem = item;
            notifyItemRangeInserted(0, callbacks.getRowCount(mItem));
        } else {
            onItemUpdated(mItem, item);
            mItem = item;
        }
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        if (!bindViewHolder(mItem, holder, position)){
            throw new IllegalStateException();
        }
    }

    public interface Callbacks<T> {
        int getRowCount(@NonNull T item);
        int getRowNumberComment();
        void changeComment(long itemId, @Nullable String currentComment);
    }
}
