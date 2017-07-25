package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.model.Item;


public abstract class ItemViewModel<Entity extends Item, Dao extends ItemDaoContract<Entity>> extends AndroidViewModel
        implements ViewModelContract<Entity> {

    private final Dao dao;
    private final LiveData<Entity> currentItem;
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private static final LiveData NO_DATA = new MutableLiveData<>();

    ItemViewModel(Application application) {
        super(application);
        dao = onCreateDao(AppDatabase.getInstance(application));
        currentItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                if (id == null){
                    //noinspection unchecked
                    return NO_DATA;
                } else return fetchItem(id);
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
    abstract Dao onCreateDao(@NonNull AppDatabase database);

    final Dao getDao() {
        return dao;
    }

    @NonNull
    @Override
    public final LiveData<Entity> getCurrentItem() {
        return currentItem;
    }

    @Override
    public final void saveNewComment(@Nullable String newComment) {
        Entity item = currentItem.getValue();
        if (item != null) {
            dao.setCommentSync(item.getId(), newComment);
        }
    }
}
