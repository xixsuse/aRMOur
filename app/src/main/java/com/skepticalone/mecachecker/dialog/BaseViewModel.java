package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

interface BaseViewModel<ItemType> {
    @NonNull
    LiveData<ItemType> getCurrentItem();
}
