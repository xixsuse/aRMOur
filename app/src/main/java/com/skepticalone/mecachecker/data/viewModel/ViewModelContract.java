package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

@MainThread
public interface ViewModelContract<Entity> {
    void selectItem(long id);
    void deleteItem(long id);
    void restoreItem(@NonNull Entity item);
    @NonNull
    LiveData<Entity> getCurrentItem();
    @NonNull
    LiveData<List<Entity>> getItems();
    void saveNewComment(long id, @Nullable String newComment);
    @NonNull
    LiveData<Entity> getDeletedItem();
    @NonNull
    LiveData<Integer> getErrorMessage();
}
