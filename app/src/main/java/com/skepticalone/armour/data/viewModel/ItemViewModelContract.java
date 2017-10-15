package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.skepticalone.armour.ui.list.DeletedItemsInfo;

import java.util.List;

public interface ItemViewModelContract<FinalItem> {

    @NonNull
    LiveData<List<FinalItem>> getItems();

    void setCurrentItemId(@Nullable Long id);

    @NonNull
    LiveData<FinalItem> getCurrentItem();

    void saveNewComment(@Nullable String comment);

    @NonNull
    LiveData<Integer> getErrorMessage();

    @NonNull
    LiveData<DeletedItemsInfo> getDeletedItemsInfo();

    @SuppressWarnings("unused")
    @NonNull
    LiveData<FinalItem> fetchItem(long id);

    @NonNull
    String getTitle(int count);

    @NonNull
    SparseBooleanArray getSelectedPositions();

    void deleteItems(@NonNull RecyclerView.Adapter adapter);

}
