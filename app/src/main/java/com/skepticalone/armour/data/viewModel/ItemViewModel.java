package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.ItemDao;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.model.LiveTimeZone;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.ZoneId;

import java.util.List;

abstract class ItemViewModel<Entity, FinalItem extends Item> extends AndroidViewModel implements ItemViewModelContract<FinalItem> {

    private static final MutableLiveData NO_DATA = new MutableLiveData<>();

    static {
        //noinspection unchecked
        NO_DATA.setValue(null);
    }

    private final LiveData<FinalItem> currentItem;
    @NonNull
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<View.OnClickListener> deletedItemRestorer = new MutableLiveData<>();
    ItemViewModel(@NonNull Application application) {
        super(application);
        currentItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<FinalItem>>() {
            @Override
            public LiveData<FinalItem> apply(Long id) {
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
    public LiveData<FinalItem> getCurrentItem() {
        return currentItem;
    }

    @NonNull
    abstract ItemDao<Entity> getDao();

    @Override
    public final void deleteItem(final long id) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                final Entity item = getDao().deleteAndReturnItemSync(id);
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
                                        long id = getDao().insertSync(item);
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
    final SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplication());
    }

    @NonNull
    final ZoneId getFreshTimezone() {
        return LiveTimeZone.getInstance(getApplication()).getNewValue(getDefaultSharedPreferences());
    }

    @NonNull
    final Shift.ShiftType.Configuration getFreshShiftConfiguration() {
        return Shift.ShiftType.LiveShiftConfig.getInstance(getApplication()).getNewValue(getDefaultSharedPreferences());
    }

    @NonNull
    @Override
    public final LiveData<FinalItem> fetchItem(final long id) {
        return Transformations.map(getItems(), new Function<List<FinalItem>, FinalItem>() {
            @Override
            public FinalItem apply(List<FinalItem> items) {
                for (FinalItem item: items) {
                    if (item.getId() == id) return item;
                }
                return null;
            }
        });
    }
}
