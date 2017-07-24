package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface BaseViewModel<Entity> {
    @NonNull
    LiveData<Entity> getCurrentItem();
    void saveNewComment(@Nullable String newComment);
}
