package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

abstract class ModelComposition<Entity> extends Composition implements Model<Entity> {

    private static final MutableLiveData NO_ITEM = new MutableLiveData<>();
    private static final MutableLiveData<List> NO_ITEMS = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final MutableLiveData<Entity> lastDeletedItem = new MutableLiveData<>();
    private final LiveData<Entity> selectedItem;
    private final ItemDao<Entity> dao;

    ModelComposition(Application application, ItemDao<Entity> dao) {
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

    @Override
    public final void insertItem(@NonNull final Entity item) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.insertItemSync(item);
            }
        });
    }

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
    public final MutableLiveData<Entity> getLastDeletedItem() {
        return lastDeletedItem;
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
    public final void deleteItem(long id) {
        new Thread(new DeleteTask<>(dao, id, lastDeletedItem)).start();
//        runAsync(new SQLiteTask() {
//            @Override
//            public void runSQLiteTask() throws ShiftOverlapException {
//                dao.deleteItemSync(id);
//            }
//        });
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

    static class DeleteTask<T> implements Runnable {

        private final ItemDao<T> dao;
        private final long id;
        private final MutableLiveData<T> deletedItem;

        DeleteTask(ItemDao<T> dao, long id, MutableLiveData<T> deletedItem) {
            this.dao = dao;
            this.id = id;
            this.deletedItem = deletedItem;
        }

        @WorkerThread
        @Override
        public void run() {
            T item = dao.getItemSync(id);
            if (item != null && dao.deleteItemSync(id) == 1) {
                deletedItem.postValue(item);
            }
        }
    }
}
