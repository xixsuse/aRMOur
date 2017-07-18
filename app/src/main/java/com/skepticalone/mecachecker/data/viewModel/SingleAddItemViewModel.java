package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.ItemDaoContract;

public abstract class SingleAddItemViewModel<Entity, Dao extends ItemDaoContract<Entity>> extends ItemViewModel<Entity, Dao> {

    SingleAddItemViewModel(@NonNull Application application, @NonNull Dao dao) {
        super(application, dao);
    }

    @MainThread
    public abstract void addNewItem();

}