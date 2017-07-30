package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.LocalDate;

abstract class DateDetailAdapterHelper<Entity> {

    @NonNull
    private final Callbacks callbacks;

    DateDetailAdapterHelper(@NonNull Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @CallSuper
    void onItemUpdated(@NonNull Entity oldItem, @NonNull Entity newItem, @NonNull RecyclerView.Adapter adapter) {
        if (!Comparators.equalLocalDates(getDate(oldItem), getDate(newItem))){
            adapter.notifyItemChanged(getRowNumberDate());
        }
    }

    @CallSuper
    boolean bindViewHolder(@NonNull Entity item, ItemViewHolder holder, int position) {
        if (position == getRowNumberDate()) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate();
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(getDate(item)));
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
