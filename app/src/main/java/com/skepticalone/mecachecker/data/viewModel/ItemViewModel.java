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
import android.support.annotation.WorkerThread;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.model.Item;

import java.util.List;


public abstract class ItemViewModel<Entity extends Item, Dao extends ItemDaoContract<Entity>> extends AndroidViewModel
        implements ViewModelContract<Entity> {

    private final Dao dao;
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
        dao = onCreateDao(AppDatabase.getInstance(application));
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

    @NonNull
    LiveData<Entity> getItem(long id) {
        return dao.getItem(id);
    }

    @MainThread
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @NonNull
    @Override
    public LiveData<View.OnClickListener> getDeletedItemRestorer() {
        return deletedItemRestorer;
    }

    @NonNull
    @Override
    public LiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    @NonNull
    abstract Dao onCreateDao(@NonNull AppDatabase database);

    final Dao getDao() {
        return dao;
    }

    @Override
    public final void restoreItem(@NonNull final Entity item) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(dao.insertItemSync(item));
            }
        });
    }

    @NonNull
    @Override
    public LiveData<List<Entity>> getItems() {
        return dao.getItems();
    }

    @NonNull
    @Override
    public final LiveData<Entity> getCurrentItem() {
        return currentItem;
    }

    @Override
    public final void deleteItem(final long id) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                final Entity item = dao.getItemSync(id);
                if (item != null && dao.deleteItemSync(item) == 1) {
                    postSelectedId(null);
                    deletedItemRestorer.postValue(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            runAsync(new Runnable() {
                                @Override
                                public void run() {
                                    deletedItemRestorer.postValue(null);
                                    postSelectedId(dao.insertItemSync(item));
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @WorkerThread
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

    @Override
    public final void saveNewComment(final long id, @Nullable final String newComment) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setCommentSync(id, newComment);
            }
        });
    }

}
