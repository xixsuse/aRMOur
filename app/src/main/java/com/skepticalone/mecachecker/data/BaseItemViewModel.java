package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface BaseItemViewModel<Entity> {

    @MainThread
    @NonNull
    LiveData<List<Entity>> getItems();

    @MainThread
    @NonNull
    LiveData<Entity> getItem(long id);

    @MainThread
    @NonNull
    LiveData<Entity> getSelectedItem();

    @MainThread
    void selectItem(long id);

    @MainThread
    void deleteItem(long id);

    @MainThread
    void setComment(long id, @Nullable String comment);

}
