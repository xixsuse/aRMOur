package com.skepticalone.mecachecker.dialog;

import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.model.Item;

public interface CommentViewModel<ItemType extends Item> extends BaseViewModel<ItemType> {
    void saveNewComment(@Nullable String newComment);
}
