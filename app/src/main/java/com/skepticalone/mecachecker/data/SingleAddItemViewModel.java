package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;

public interface SingleAddItemViewModel<T> extends BaseItemViewModel<T> {

    @MainThread
    void addNewItem();

}
