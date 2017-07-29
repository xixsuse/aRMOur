package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface ViewModelContract<Entity> {
    void selectItem(long id);
    void deleteItem(long id);
    void insertItem(@NonNull Entity item);
    @NonNull
    LiveData<Entity> getCurrentItem();
    @NonNull
    LiveData<List<Entity>> getItems();
    void saveNewComment(@Nullable String newComment);
    @NonNull
    LiveData<Entity> getDeletedItem();
    @NonNull
    LiveData<Integer> getErrorMessage();
}
