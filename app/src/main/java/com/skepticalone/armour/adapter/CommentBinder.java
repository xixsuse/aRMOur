package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;

final class CommentBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @Nullable
    private final String comment;

    CommentBinder(@NonNull Callbacks callbacks, @Nullable String comment) {
        this.callbacks = callbacks;
        this.comment = comment;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        CommentBinder newBinder = (CommentBinder) other;
        return comment == null ? newBinder.comment == null : comment.equals(newBinder.comment);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_comment_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.comment);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return comment;
    }

    @Override
    public void onClick(View v) {
        callbacks.changeComment();
    }

    interface Callbacks {
        void changeComment();
    }

}
