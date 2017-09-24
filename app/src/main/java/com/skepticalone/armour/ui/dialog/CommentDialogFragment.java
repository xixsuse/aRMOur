package com.skepticalone.armour.ui.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;

public abstract class CommentDialogFragment<Entity extends Item> extends PlainTextDialogFragment<Entity> {

    @Override
    final int getTitle() {
        return R.string.comment;
    }

    @Nullable
    @Override
    final String getTextForDisplay(@NonNull Entity item) {
        return item.getComment();
    }

    @Override
    final void saveText(@Nullable String comment) {
        getViewModel().saveNewComment(comment);
    }

}
