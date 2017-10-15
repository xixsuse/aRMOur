package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.skepticalone.armour.ui.list.DeletedItemsInfo;

import java.util.List;

public interface ItemViewModelContract<FinalItem> {

    @NonNull
    LiveData<List<FinalItem>> getAllItems();

    @NonNull
    LiveData<List<FinalItem>> getSelectedItems();

    void setCurrentItemId(@Nullable Long id);

    @NonNull
    LiveData<FinalItem> getCurrentItem();

    void saveNewComment(@Nullable String comment);

    @NonNull
    LiveData<Integer> getErrorMessage();

    @NonNull
    LiveData<DeletedItemsInfo> getDeletedItemsInfo();

    @NonNull
    String getTitle(int count);

    @NonNull
    LiveData<SparseBooleanArray> getSelectedPositions();

    void setSelectedPositions(@NonNull SparseBooleanArray selectedPositions);

    void deleteItems();

}
