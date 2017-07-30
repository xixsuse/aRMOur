package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

@SuppressWarnings("NullableProblems")
public interface ItemDaoContract<Entity> {

    @WorkerThread
    long insertItemSync(@NonNull Entity item);

    @MainThread
    @NonNull
    LiveData<List<Entity>> getItems();

    @MainThread
    @NonNull
    LiveData<Entity> getItem(long id);

    @WorkerThread
    @Nullable
    Entity getItemSync(long id);

    @WorkerThread
    int deleteItemSync(@NonNull Entity item);

    @WorkerThread
    void setCommentSync(long id, @Nullable String comment);

}
