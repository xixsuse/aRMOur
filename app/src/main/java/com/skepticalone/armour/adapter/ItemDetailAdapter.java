package com.skepticalone.armour.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.util.Comparators;

public abstract class ItemDetailAdapter<Entity extends Item> extends ContextAdapter implements Observer<Entity> {

    @NonNull
    private final Callbacks callbacks;
    @Nullable
    private Entity mItem;

    ItemDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @Override
    public final int getItemCount() {
        return mItem == null ? 0 : getRowCount(mItem);
    }

    @CallSuper
    void onItemUpdated(@NonNull Entity oldItem, @NonNull Entity newItem) {
        if (!Comparators.equalStrings(oldItem.getComment(), newItem.getComment())) {
            notifyItemChanged(getRowNumberComment(oldItem));
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @CallSuper
    boolean bindViewHolder(@NonNull Entity item, ItemViewHolder holder, int position) {
        if (position == getRowNumberComment(item)) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_comment_black_24dp);
            holder.setText(getContext().getString(R.string.comment), item.getComment());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeComment();
                }
            });
            return true;
        } else return false;
    }

    @Override
    public final void onChanged(@Nullable Entity item) {
        if (mItem == null && item == null) {
            return;
        } else if (mItem == null) {
            notifyItemRangeInserted(0, getRowCount(item));
        } else if (item == null) {
            notifyItemRangeRemoved(0, getRowCount(mItem));
        } else {
            onItemUpdated(mItem, item);
        }
        mItem = item;
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        if (!bindViewHolder(mItem, holder, position)) {
            throw new IllegalStateException();
        }
    }
    abstract int getRowCount(@NonNull Entity item);
    abstract int getRowNumberComment(@NonNull Entity item);

    public interface Callbacks {
        void changeComment();
    }
}
