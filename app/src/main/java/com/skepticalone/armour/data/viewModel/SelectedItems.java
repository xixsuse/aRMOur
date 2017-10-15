package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

final class SelectedItems<FinalItem> extends MediatorLiveData<List<FinalItem>> {

    SelectedItems(@NonNull final LiveData<List<FinalItem>> allItems, @NonNull final LiveData<SparseBooleanArray> selectedPositions) {
        super();
        addSource(allItems, new Observer<List<FinalItem>>() {
            @Override
            public void onChanged(@Nullable List<FinalItem> allItems) {
                SelectedItems.this.onChanged(allItems, selectedPositions.getValue());
            }
        });
        addSource(selectedPositions, new Observer<SparseBooleanArray>() {
            @Override
            public void onChanged(@Nullable SparseBooleanArray selectedPositions) {
                SelectedItems.this.onChanged(allItems.getValue(), selectedPositions);
            }
        });
    }

    private void onChanged(@Nullable List<FinalItem> allItems, @Nullable SparseBooleanArray selectedPositions) {
        if (allItems != null && selectedPositions != null) {
            int selectedCount = selectedPositions.size();
            if (selectedCount > 0) {
                List<FinalItem> selectedItems = new ArrayList<>(selectedCount);
                for (int i = 0; i < selectedCount; i++) {
                    if (selectedPositions.valueAt(i)) {
                        //noinspection ConstantConditions
                        selectedItems.add(allItems.get(selectedPositions.keyAt(i)));
                    }
                }
                if (!selectedItems.isEmpty()) {
                    setValue(selectedItems);
                    return;
                }
            }
            setValue(null);
        }
    }

}
