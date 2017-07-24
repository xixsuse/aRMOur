package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.dialog.IndependentCommentDialogFragment;

import java.util.List;


public abstract class ItemViewModel<Entity extends Item> extends AndroidViewModel implements IndependentCommentDialogFragment.ViewModelCallbacks {

    final LiveData NO_DATA = new MutableLiveData<>();
    final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    public final EntityObservable<Entity> lastDeletedItem = new EntityObservable<>();
    public final ErrorMessageObservable errorMessage = new ErrorMessageObservable();
    public final LiveData<Entity> selectedItem;
    private final LiveData<String> currentComment;

    ItemViewModel(@NonNull Application application) {
        super(application);
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                return id == null ? NO_DATA : getItem(id);
            }
        });
        currentComment = Transformations.map(selectedItem, new Function<Entity, String>() {
            @Override
            public String apply(Entity item) {
                return item == null ? null : item.getComment();
            }
        });

    }

    @NonNull
    LiveData<Entity> getItem(long id) {
        return getDao().getItem(id);
    }
//
//    @NonNull
//    Function<Long, LiveData<Entity>> createTransformation(LiveData<Entity> noItem) {
//        return new Function<Long, LiveData<Entity>>() {
//            @Override
//            public LiveData<Entity> apply(Long id) {
//                return id == null ? NO_DATA : getDao().getItem(id);
//            }
//        };
//    }

    @NonNull
    abstract ItemDaoContract<Entity> getDao();
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }
    @MainThread
    @NonNull
    public LiveData<List<Entity>> getItems(){
        return getDao().getItems();
    }
//    @MainThread
//    @NonNull
//    public final LiveData<Entity> getItem(long id) {
//        return getDao().getItem(id);
//    }
    static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @NonNull
    @Override
    public LiveData<String> getCurrentComment() {
        return currentComment;
    }

    @Override
    public void saveNewComment(@Nullable String newComment) {
        runAsync(new SetCommentTask(dao, selectedId.getValue(), newComment));
    }

    @MainThread
    public final void insertItem(@NonNull Entity item) {
        runAsync(new InsertItemTask<>(getDao(), item));
    }
    @MainThread
    public final void deleteItem(final long id) {
        runAsync(new DeleteItemTask<>(getDao(), id, lastDeletedItem));
    }
    @MainThread
    public final void setComment(long id, @Nullable final String comment){
        runAsync(new SetCommentTask(getDao(), id, comment));
    }
    static final class SetCommentTask extends ItemRunnable<ItemDaoContract> {
        @Nullable
        private final String comment;
        SetCommentTask(@NonNull ItemDaoContract dao, long id, @Nullable String comment) {
            super(dao, id);
            this.comment = comment;
        }
        @Override
        void run(@NonNull ItemDaoContract dao, long id) {
            dao.setCommentSync(id, comment);
        }
    }
    static final class InsertItemTask<Entity> extends DaoRunnable<ItemDaoContract<Entity>> {
        @NonNull final Entity item;
        InsertItemTask(@NonNull ItemDaoContract<Entity> dao, @NonNull Entity item) {
            super(dao);
            this.item = item;
        }
        @Override
        void run(@NonNull ItemDaoContract<Entity> dao) {
            dao.insertItemSync(item);
        }
    }
    static final class DeleteItemTask<Entity> extends ItemRunnable<ItemDaoContract<Entity>> {
        private final EntityObservable<Entity> lastDeletedItem;
        DeleteItemTask(@NonNull ItemDaoContract<Entity> dao, long id, EntityObservable<Entity> lastDeletedItem) {
            super(dao, id);
            this.lastDeletedItem = lastDeletedItem;
        }
        @Override
        void run(@NonNull ItemDaoContract<Entity> dao, long id) {
            Entity item = dao.getItemSync(id);
            if (item != null && dao.deleteItemSync(item) == 1) {
                lastDeletedItem.setItem(item);
            }
        }
    }
    abstract static class OverlapItemRunnable<Dao> extends ItemRunnable<Dao> {
        @NonNull
        private final ErrorMessageObservable errorMessage;
        OverlapItemRunnable(@NonNull Dao dao, long id, @NonNull ErrorMessageObservable errorMessage) {
            super(dao, id);
            this.errorMessage = errorMessage;
        }
        @Override
        final void run(@NonNull Dao dao, long id) {
            try {
                runOrThrow(dao, id);
            } catch (SQLiteConstraintException e) {
                errorMessage.setErrorMessage(R.string.overlapping_shifts);
            }
        }
        @WorkerThread
        abstract void runOrThrow(@NonNull Dao dao, long id) throws SQLiteConstraintException;
    }
    abstract static class ItemRunnable<Dao> extends DaoRunnable<Dao> {
        private final long id;
        ItemRunnable(@NonNull Dao dao, long id) {
            super(dao);
            this.id = id;
        }
        @Override
        final void run(@NonNull Dao dao) {
            run(dao, id);
        }
        @WorkerThread
        abstract void run(@NonNull Dao dao, long id);
    }
    abstract static class DaoRunnable<Dao> implements Runnable {
        @NonNull
        private final Dao dao;
        DaoRunnable(@NonNull Dao dao) {
            this.dao = dao;
        }
        @Override
        public final void run() {
            run(dao);
        }
        abstract void run(@NonNull Dao dao);
    }
}
