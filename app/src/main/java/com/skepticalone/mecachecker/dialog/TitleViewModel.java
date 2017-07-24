package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.Item;

public interface TitleViewModel<ItemType extends Item> extends BaseViewModel<ItemType> {
    void saveNewTitle(@NonNull String newTitle);
}
