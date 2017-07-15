package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.model.Item;

import java.util.List;

interface BaseItemDao<ItemType extends Item> {

    @WorkerThread
    void insertItemSync(ItemType item);

    @MainThread
    @NonNull
    LiveData<List<ItemType>> getItems();

    @MainThread
    @NonNull
    LiveData<ItemType> getItem(long id);

    @WorkerThread
    void deleteItemSync(long id);

    @WorkerThread
    void setCommentSync(long id, @Nullable String comment);

}
