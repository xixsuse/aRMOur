package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

abstract class DateDetailAdapterHelper<Entity> {

    @NonNull
    private final Callbacks callbacks;

    DateDetailAdapterHelper(@NonNull Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @CallSuper
    void onItemUpdated(@NonNull Entity oldItem, @NonNull Entity newItem, @NonNull RecyclerView.Adapter adapter) {
        if (!getDate(oldItem).isEqual(getDate(newItem))) {
            adapter.notifyItemChanged(getRowNumberDate());
        }
    }

    @CallSuper
    boolean bindViewHolder(@NonNull ContextAdapter adapter, @NonNull Entity item, ItemViewHolder holder, int position) {
        if (position == getRowNumberDate()) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_calendar_black_24dp);
            holder.setText(adapter.getContext().getString(R.string.date), DateTimeUtils.getFullDateString(getDate(item)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate();
                }
            });
            return true;
        } else return false;
    }

    abstract int getRowNumberDate();
    @NonNull
    abstract LocalDate getDate(@NonNull Entity item);

    interface Callbacks {
        void changeDate();
    }

}
