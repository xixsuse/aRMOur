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
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CustomDao;

import java.util.List;


public abstract class ItemViewModel<Entity> extends AndroidViewModel {

    private final LiveData<Entity> currentItem;
    @NonNull
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private static final MutableLiveData NO_DATA = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<View.OnClickListener> deletedItemRestorer = new MutableLiveData<>();

    static {
        //noinspection unchecked
        NO_DATA.setValue(null);
    }
    ItemViewModel(Application application) {
        super(application);
//        dao = onCreateDao(AppDatabase.getInstance(application));
        currentItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                if (id == null){
                    //noinspection unchecked
                    return NO_DATA;
                } else {
                    return getItem(id);
                }
            }
        });
    }

    @NonNull
    abstract CustomDao<Entity> getDao();

    @NonNull
    LiveData<Entity> getItem(long id) {
        return getDao().getItem(id);
    }

    void runAsync(final Runnable runnable) {
        new Thread(runnable).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    runnable.run();
//                } catch (InterruptedException e) {
//                    // do nothing
//                }
//            }
//        }).start();
    }

    @MainThread
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @NonNull
    public LiveData<View.OnClickListener> getDeletedItemRestorer() {
        return deletedItemRestorer;
    }

    @NonNull
    public LiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

//    public final void restoreItem(@NonNull final Entity item) {
//        runAsync(new Runnable() {
//            @Override
//            public void run() {
//                postSelectedId(dao.insertItemSync(item));
//            }
//        });
//    }

    @NonNull
    public LiveData<List<Entity>> getItems() {
        return getDao().getItems();
    }

    @NonNull
    public final LiveData<Entity> getCurrentItem() {
        return currentItem;
    }

    public final void deleteItem(final long id) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                final Entity item = getDao().deleteSync(id);
                if (item != null) {
                    postSelectedId(null);
                    deletedItemRestorer.postValue(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            runAsync(new Runnable() {
                                @Override
                                public void run() {
                                    deletedItemRestorer.postValue(null);
                                    try {
                                        long id = getDao().restoreItemSync(item);
                                        postSelectedId(id);
                                    } catch (SQLiteConstraintException e) {
                                        postOverlappingShifts();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

//    public final class DeletedItemRestorerClickListener implements

    final void postOverlappingShifts() {
        errorMessage.postValue(R.string.overlapping_shifts);
    }

    @WorkerThread
    final void postSelectedId(@Nullable Long id) {
        selectedId.postValue(id);
    }
//    @NonNull
//    final Entity getCurrentItemSync() {
//        Entity item = currentItem.getValue();
//        assert (item != null);
//        return item;
//    }
//
//    final long getCurrentItemId() {
//        return getCurrentItemSync().getId();
//    }

    public void saveNewComment(final long id, @Nullable final String newComment) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setCommentSync(id, newComment);
            }
        });
    }

}
