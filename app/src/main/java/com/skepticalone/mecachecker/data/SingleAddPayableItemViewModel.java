package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.model.PayableItem;

public abstract class SingleAddPayableItemViewModel<Entity extends PayableItem> extends PayableItemViewModel<Entity> {

    SingleAddPayableItemViewModel(Application application) {
        super(application);
    }

    @WorkerThread
    @NonNull
    abstract Entity generateNewItemSync();

    @MainThread
    public final void addItem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertItemSync(generateNewItemSync());
            }
        }).start();
    }

}