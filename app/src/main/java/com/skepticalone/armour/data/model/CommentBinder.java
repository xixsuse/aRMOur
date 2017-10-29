package com.skepticalone.armour.data.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;

public final class CommentBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @Nullable
    private final String comment;

    public CommentBinder(@NonNull Callbacks callbacks, @Nullable String comment) {
        this.callbacks = callbacks;
        this.comment = comment;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        CommentBinder newBinder = (CommentBinder) other;
        return comment == null ? newBinder.comment == null : comment.equals(newBinder.comment);
    }

    @Override
    public int getPrimaryIcon() {
        return R.drawable.ic_comment_black_24dp;
    }

    @NonNull
    @Override
    public String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.comment);
    }

    @Nullable
    @Override
    public String getSecondLine(@NonNull Context context) {
        return comment;
    }

    @Override
    public void onClick(View v) {
        callbacks.changeComment();
    }

    public interface Callbacks {
        void changeComment();
    }

}
