package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import java.util.List;

public interface BaseItemViewModel<Entity> {

    LiveData<List<Entity>> getItems();

    LiveData<Entity> getItem(long id);

    LiveData<Entity> getSelectedItem();

    void selectItem(long id);

    @MainThread
    void deleteItem(long id);

    @MainThread
    void setComment(long id, @Nullable String comment);

}
