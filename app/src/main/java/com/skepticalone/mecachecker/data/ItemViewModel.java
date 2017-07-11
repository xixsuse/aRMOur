package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.content.LocalBroadcastManager;

import com.skepticalone.mecachecker.model.Item;

import java.util.List;

public abstract class ItemViewModel<Entity extends Item> extends AndroidViewModel {

    public static final String DISPLAY_ERROR = "com.skepticalone.mecachecker.DISPLAY_ERROR";
    private static final MutableLiveData NO_ITEM = new MutableLiveData<>();
    private static final MutableLiveData<List> NO_ITEMS = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final LiveData<Entity> selectedItem;
    private final LocalBroadcastManager mLocalBroadcastManager;

    ItemViewModel(Application application) {
        super(application);
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                //noinspection unchecked
                return id == null ? NO_ITEM : getItem(id);
            }
        });
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(application);
    }

    public final LiveData<Entity> getSelectedItem() {
        return selectedItem;
    }

    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    public abstract LiveData<List<Entity>> getItems();

    abstract LiveData<Entity> getItem(long id);

    @WorkerThread
    abstract void insertItemSync(@NonNull Entity item);

    @MainThread
    public final void insertItem(@NonNull final Entity item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertItemSync(item);
            }
        }).start();
    }

    abstract Entity generateRandomItem();

    @MainThread
    public final void insertRandomItem() {
        // TODO: 11/07/17 remove this method
        insertItem(generateRandomItem());
    }

    @WorkerThread
    abstract void deleteItemSync(long id);

    @MainThread
    public final void deleteItem(final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteItemSync(id);
            }
        }).start();
    }

    interface SQLiteTask {
        @WorkerThread
        void runSQLiteTask() throws SQLiteConstraintException;

        @NonNull
        String getErrorMessage();
    }

    final class SQLiteThread extends Thread {

        private final SQLiteTask task;

        SQLiteThread(SQLiteTask task) {
            super();
            this.task = task;
        }

        @Override
        public final void run() {
            try {
                task.runSQLiteTask();
            } catch (SQLiteConstraintException e) {
                Intent intent = new Intent();
                intent.setAction(DISPLAY_ERROR);
                intent.putExtra(Intent.EXTRA_TEXT, task.getErrorMessage());
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        }

    }

//
//    abstract static class CreateTask<T> extends AsyncTask<T, Void, Boolean> {
//
//        @SafeVarargs
//        @Override
//        protected final Boolean doInBackground(T... items) {
//            try {
//                for (T item : items) {
//                    insertItem(item);
//                }
//                return true;
//            } catch (SQLiteConstraintException e) {
//                return false;
//            }
//        }
//
//        @WorkerThread
//        abstract void insertItem(T item);
//    }
//
//    abstract static class DeleteTask extends AsyncTask<Long, Void, Boolean> {
//
//        @Override
//        final protected Boolean doInBackground(Long... itemIds) {
//            try {
//                for (long itemId : itemIds) {
//                    deleteItem(itemId);
//                }
//                return true;
//            } catch (SQLiteConstraintException e) {
//                return false;
//            }
//        }
//
//        @WorkerThread
//        abstract void deleteItem(long itemId);
//
//    }

}
