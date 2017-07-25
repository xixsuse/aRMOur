package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.DeletedItem;

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
    DeletedItem.Observable<Entity> getDeletedItem();
}
