package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;

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
    private final MutableLiveData<Entity> deletedItem = new MutableLiveData<>();
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
                    return fetchItem(id);
                }
            }
        });
    }

    @NonNull
    LiveData<Entity> fetchItem(long id) {
        return dao.getItem(id);
    }

    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    @NonNull
    @Override
    public LiveData<Entity> getDeletedItem() {
        return deletedItem;
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
    public final void insertItem(@NonNull Entity item) {
        selectedId.setValue(dao.insertItemSync(item));
    }

    @NonNull
    @Override
    public final LiveData<List<Entity>> getItems() {
        return dao.getItems();
    }

    @NonNull
    @Override
    public final LiveData<Entity> getCurrentItem() {
        return currentItem;
    }

    @Override
    public final void deleteItem(long id) {
        Entity item = dao.getItemSync(id);
        if (item != null && dao.deleteItemSync(item) == 1) {
            selectedId.setValue(null);
            deletedItem.setValue(item);
        }
    }

    @Override
    public final void saveNewComment(@Nullable String newComment) {
        Entity item = currentItem.getValue();
        if (item != null) {
            dao.setCommentSync(item.getId(), newComment);
        }
    }

    @WorkerThread
    final void postErrorMessage(@StringRes int errorMessage) {
        this.errorMessage.postValue(errorMessage);
    }

}
