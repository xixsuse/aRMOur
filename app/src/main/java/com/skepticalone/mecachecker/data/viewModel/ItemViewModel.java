package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.dao.ItemDaoContract;

import java.util.List;


public abstract class ItemViewModel<Entity> extends AndroidViewModel {

    private final LiveData<Entity> NO_ITEM = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    public final MutableLiveData<Entity> lastDeletedItem = new MutableLiveData<>();
    public final LiveData<Entity> selectedItem;

    ItemViewModel(@NonNull Application application) {
        super(application);
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                return id == null ? NO_ITEM : getItem(id);
            }
        });
    }

    @NonNull
    abstract ItemDaoContract<Entity> getDao();

    static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @MainThread
    public final void insertItem(@NonNull final Entity item) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().insertItemSync(item);
            }
        });
    }

    @MainThread
    @NonNull
    public final LiveData<List<Entity>> getItems(){
        return getDao().getItems();
    }

    @MainThread
    @NonNull
    public final LiveData<Entity> getItem(long id) {
        return getDao().getItem(id);
    }

    @MainThread
    public final void deleteItem(final long id) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                Entity item = getDao().getItemSync(id);
                if (item != null && getDao().deleteItemSync(id) == 1) {
                    lastDeletedItem.postValue(item);
                }
            }
        });
    }

    @MainThread
    public final void setComment(final long id, @Nullable final String comment){
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setCommentSync(id, comment);
            }
        });
    }


//    final void runAsync(SQLiteTask task) {
//        new DatabaseOperation(getApplication()).execute(task);
//    }

//    static private final class DatabaseOperation extends AsyncTask<SQLiteTask, Void, Void> {
//
//        private final LocalBroadcastManager mBroadcastManager;
//
//        DatabaseOperation(Application application) {
//            super();
//            mBroadcastManager = LocalBroadcastManager.getInstance(application);
//        }
//
//        @Override
//        protected final Void doInBackground(SQLiteTask[] tasks) {
//            for (SQLiteTask task : tasks) {
//                try {
//                    task.runSQLiteTask();
//                } catch (ShiftOverlapException e) {
//                    Intent intent = new Intent();
//                    intent.setAction(Constants.DISPLAY_ERROR);
//                    intent.putExtra(Intent.EXTRA_TEXT, e.getMessage());
//                    mBroadcastManager.sendBroadcast(intent);
//                }
//                if (isCancelled()) break;
//            }
//            return null;
//        }
//
//    }
//
//    static class DeleteTask<T> implements Runnable {
//
//        private final ItemDao<T> dao;
//        private final long id;
//        private final MutableLiveData<T> deletedItem;
//
//        DeleteTask(ItemDao<T> dao, long id, MutableLiveData<T> deletedItem) {
//            this.dao = dao;
//            this.id = id;
//            this.deletedItem = deletedItem;
//        }
//
//        @WorkerThread
//        @Override
//        public void run() {
//            T item = dao.getItemSync(id);
//            if (item != null && dao.deleteItemSync(id) == 1) {
//                deletedItem.postValue(item);
//            }
//        }
//    }
}
