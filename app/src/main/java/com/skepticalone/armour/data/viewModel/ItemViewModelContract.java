package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;
import java.util.Set;

public interface ItemViewModelContract<FinalItem> {
    @NonNull
    LiveData<List<FinalItem>> getItems();
    void selectItem(long id);
    @NonNull
    LiveData<FinalItem> getCurrentItem();
    void saveNewComment(@Nullable String comment);
    @NonNull
    LiveData<Integer> getErrorMessage();

    void toggleSelected(long id);

    void deleteSelectedItems();

    boolean hasSelectedItems();

    @NonNull
    Set<Long> getSelectedIds();

    @NonNull
    LiveData<View.OnClickListener> getDeletedItemRestorer();

    @SuppressWarnings("unused")
    @NonNull
    LiveData<FinalItem> fetchItem(long id);

}
