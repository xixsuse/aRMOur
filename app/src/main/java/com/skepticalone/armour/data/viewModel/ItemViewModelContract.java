package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;

import com.skepticalone.armour.ui.list.DeletedItemsInfo;

import java.util.List;
import java.util.Set;

public interface ItemViewModelContract<FinalItem> {

    @NonNull
    LiveData<List<FinalItem>> getItems();

    void setCurrentItemId(@Nullable Long id);

    @NonNull
    LiveData<FinalItem> getCurrentItem();

    void saveNewComment(@Nullable String comment);

    @NonNull
    LiveData<Integer> getErrorMessage();

    void deleteItems(@NonNull Set<Long> itemIds, @PluralsRes int quantityStringResource);

    @NonNull
    LiveData<DeletedItemsInfo> getDeletedItemsInfo();

    @SuppressWarnings("unused")
    @NonNull
    LiveData<FinalItem> fetchItem(long id);

}
