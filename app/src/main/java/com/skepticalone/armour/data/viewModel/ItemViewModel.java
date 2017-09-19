package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.ItemDao;
import com.skepticalone.armour.data.entity.LiveShiftTypeCalculator;

import org.threeten.bp.ZoneId;

import java.util.List;


abstract class ItemViewModel<Entity> extends AndroidViewModel implements ItemViewModelContract<Entity> {

    private static final MutableLiveData NO_DATA = new MutableLiveData<>();

    static {
        //noinspection unchecked
        NO_DATA.setValue(null);
    }

    private final LiveData<Entity> currentItem;
    @NonNull
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<View.OnClickListener> deletedItemRestorer = new MutableLiveData<>();
    ItemViewModel(@NonNull Application application) {
        super(application);
        currentItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                if (id == null){
                    //noinspection unchecked
                    return NO_DATA;
                } else {
                    return fetchItem(id);
                }
            }
        });
    }

    static void runAsync(final Runnable runnable) {
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
    abstract ItemDao<Entity> getDao();

    @NonNull
    LiveData<Entity> fetchItem(long id) {
        return getDao().getItem(id);
    }

    final long getCurrentItemId() {
        Long id = selectedId.getValue();
        if (id == null) throw new IllegalStateException();
        return id;
    }

    @Override
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @NonNull
    @Override
    public final LiveData<View.OnClickListener> getDeletedItemRestorer() {
        return deletedItemRestorer;
    }

    @NonNull
    @Override
    public final LiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    @NonNull
    @Override
    public LiveData<List<Entity>> getItems() {
        return getDao().getItems();
    }

    @NonNull
    @Override
    public LiveData<Entity> getCurrentItem() {
        return currentItem;
    }

    @Override
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
//                                        postOverlappingShifts();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    final void postOverlappingShifts() {
        errorMessage.postValue(R.string.overlapping_shifts);
    }

    final void postSelectedId(@Nullable Long id) {
        selectedId.postValue(id);
    }

    @Override
    public final void saveNewComment(@Nullable final String comment) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setCommentSync(getCurrentItemId(), comment);
            }
        });
    }

    @NonNull
    final ZoneId getZoneId() {
        return LiveShiftTypeCalculator.getInstance(getApplication()).getZoneId(getApplication());
    }

}
