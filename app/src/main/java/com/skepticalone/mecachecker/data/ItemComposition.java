package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Item;

import java.util.List;

abstract class ItemComposition<Entity extends Item> extends Composition implements ItemCallbacks<Entity> {

    private static final MutableLiveData NO_ITEM = new MutableLiveData<>();
    private static final MutableLiveData<List> NO_ITEMS = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final LiveData<Entity> selectedItem;
    private final ItemDao<Entity> dao;

    ItemComposition(Application application, ItemDao<Entity> dao) {
        super(application);
        this.dao = dao;
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                //noinspection unchecked
                return id == null ? NO_ITEM : getItem(id);
            }
        });
    }

//    @Override
//    public final void insertItem(@NonNull final Entity item) {
//        runAsync(new SQLiteTask() {
//            @Override
//            public void runSQLiteTask() throws ShiftOverlapException {
//                dao.insertItemSync(item);
//            }
//        });
//    }
//
    @Override
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @NonNull
    @Override
    public final LiveData<Entity> getSelectedItem() {
        return selectedItem;
    }

    @NonNull
    @Override
    public final LiveData<List<Entity>> getItems() {
        return dao.getItems();
    }

    @NonNull
    @Override
    public final LiveData<Entity> getItem(long id) {
        return dao.getItem(id);
    }

    @Override
    public final void deleteItem(final long id) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.deleteItemSync(id);
            }
        });
    }

    @Override
    public final void setComment(final long id, @Nullable final String comment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.setCommentSync(id, comment);
            }
        });
    }
}
