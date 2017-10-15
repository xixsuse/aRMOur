package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Item;

import java.util.List;

final class CurrentItem<FinalItem extends Item> extends MediatorLiveData<FinalItem> {

    CurrentItem(@NonNull final LiveData<List<FinalItem>> allItems, @NonNull final LiveData<Long> currentId) {
        super();
        addSource(allItems, new Observer<List<FinalItem>>() {
            @Override
            public void onChanged(@Nullable List<FinalItem> items) {
                CurrentItem.this.onChanged(items, currentId.getValue());
            }
        });
        addSource(currentId, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long id) {
                CurrentItem.this.onChanged(allItems.getValue(), id);
            }
        });
    }

    private void onChanged(@Nullable List<FinalItem> items, @Nullable Long id) {
        if (id == null) {
            setValue(null);
        } else if (items != null) {
            for (FinalItem item : items) {
                if (item.getId() == id) {
                    setValue(item);
                    return;
                }
            }
        }
    }

}
