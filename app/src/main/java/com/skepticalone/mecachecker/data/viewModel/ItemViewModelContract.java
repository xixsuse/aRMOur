package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface ItemViewModelContract<Entity> {
    @NonNull
    LiveData<List<Entity>> getItems();
    void selectItem(long id);
    @NonNull
    LiveData<Entity> getCurrentItem();
    void saveNewComment(@Nullable String comment);
    @NonNull
    LiveData<Integer> getErrorMessage();
}
