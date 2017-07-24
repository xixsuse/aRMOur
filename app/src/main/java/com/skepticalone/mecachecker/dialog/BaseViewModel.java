package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.model.Item;

public interface BaseViewModel<Entity extends Item> {
    @NonNull
    LiveData<Entity> getCurrentItem();
    void saveNewComment(@Nullable String newComment);
}
