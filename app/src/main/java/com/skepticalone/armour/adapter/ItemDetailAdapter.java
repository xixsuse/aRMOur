package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.util.Comparators;

public abstract class ItemDetailAdapter<FinalItem extends Item> extends ObservableAdapter<FinalItem> {
    @NonNull
    private final Callbacks callbacks;

    ItemDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @Override
    @CallSuper
    void notifyUpdated(@NonNull FinalItem oldItem, @NonNull FinalItem newItem) {
        if (!Comparators.equalStrings(oldItem.getComment(), newItem.getComment())) {
            notifyItemChanged(getRowNumberComment());
        }
    }

    @Override
    @CallSuper
    void onBindViewHolder(@NonNull FinalItem item, int position, @NonNull ItemViewHolder holder) {
        if (position == getRowNumberComment()) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_comment_black_24dp);
            holder.setText(getContext().getString(R.string.comment), item.getComment());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeComment();
                }
            });
        } else throw new IllegalStateException();
    }

    @Override
    final int getRowCount(@NonNull FinalItem item) {
        return getFixedRowCount();
    }

    abstract int getFixedRowCount();

    abstract int getRowNumberComment();

    public interface Callbacks {
        void changeComment();
    }

}
