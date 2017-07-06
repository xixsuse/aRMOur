package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.skepticalone.mecachecker.model.Item;

import java.util.List;

public abstract class ItemViewModel<Entity extends Item> extends AndroidViewModel {

    private static final MutableLiveData NO_ITEM = new MutableLiveData<>();
    private static final MutableLiveData<List> NO_ITEMS = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final LiveData<Entity> selectedItem;

    ItemViewModel(final Application application) {
        super(application);
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                //noinspection unchecked
                return id == null ? NO_ITEM : getItem(id);
            }
        });
    }

    public final LiveData<Entity> getSelectedItem() {
        return selectedItem;
    }

    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    public abstract LiveData<List<Entity>> getItems();

    abstract LiveData<Entity> getItem(long id);

    public abstract void deleteItem(long id);

}
