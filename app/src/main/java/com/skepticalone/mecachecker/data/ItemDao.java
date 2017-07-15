package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

interface ItemDao<Entity> {

    @WorkerThread
    void insertItemSync(@NonNull Entity item);

    @MainThread
    @NonNull
    LiveData<List<Entity>> getItems();

    @MainThread
    @NonNull
    LiveData<Entity> getItem(long id);

    @WorkerThread
    void deleteItemSync(long id);

    @WorkerThread
    void setCommentSync(long id, @Nullable String comment);

}
