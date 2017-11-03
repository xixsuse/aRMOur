package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.armour.R;

final class TitleBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final String title;

    TitleBinder(@NonNull Callbacks callbacks, @NonNull String title) {
        this.callbacks = callbacks;
        this.title = title;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        TitleBinder newBinder = (TitleBinder) other;
        return title.equals(newBinder.title);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_title_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.title);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return title;
    }

    @Override
    public void onClick(View v) {
        callbacks.changeTitle();
    }

    interface Callbacks {
        void changeTitle();
    }

}
