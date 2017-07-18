package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public abstract class SingleAddItemViewModel<Entity> extends ItemViewModel<Entity> {
    SingleAddItemViewModel(@NonNull Application application) {
        super(application);
    }

    @MainThread
    public abstract void addNewItem();

}