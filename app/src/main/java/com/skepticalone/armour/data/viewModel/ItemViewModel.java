package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.util.SparseBooleanArray;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.ItemDao;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.ui.list.DeletedItemsInfo;
import com.skepticalone.armour.util.LiveTimeZone;

import org.threeten.bp.ZoneId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ItemViewModel<Entity, FinalItem extends Item> extends AndroidViewModel implements ItemViewModelContract<FinalItem> {

    @NonNull
    private final LiveData<List<FinalItem>> allItems, selectedItems;

    @NonNull
    private final LiveData<FinalItem> currentItem;

    @NonNull
    private final MutableLiveData<SparseBooleanArray> selectedPositions = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Long> currentId = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<DeletedItemsInfo> deletedItemRestorer = new MutableLiveData<>();

    ItemViewModel(@NonNull Application application) {
        super(application);
        allItems = createAllItems();
        selectedPositions.setValue(new SparseBooleanArray());
        selectedItems = new SelectedItems<>(allItems, selectedPositions);
        currentItem = new CurrentItem<>(allItems, currentId);
    }

    static void runAsync(final Runnable runnable) {
        new Thread(runnable).start();
    }

    @NonNull
    abstract LiveData<List<FinalItem>> createAllItems();

    @NonNull
    @Override
    public final LiveData<List<FinalItem>> getAllItems() {
        return allItems;
    }

    @NonNull
    @Override
    public final LiveData<SparseBooleanArray> getSelectedPositions() {
        return selectedPositions;
    }

    @Override
    public void setSelectedPositions(@NonNull SparseBooleanArray positions) {
        selectedPositions.setValue(positions);
    }

    final long getCurrentItemId() {
        Long id = currentId.getValue();
        if (id == null) throw new IllegalStateException();
        return id;
    }

    @Override
    public final void setCurrentItemId(@Nullable Long id) {
        currentId.setValue(id);
    }

    @NonNull
    @Override
    public final LiveData<DeletedItemsInfo> getDeletedItemsInfo() {
        return deletedItemRestorer;
    }

    @NonNull
    @Override
    public final LiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    @NonNull
    @Override
    public final LiveData<FinalItem> getCurrentItem() {
        return currentItem;
    }

    @NonNull
    abstract ItemDao<Entity> getDao();

    final void postOverlappingShifts() {
        errorMessage.postValue(R.string.overlapping_shifts);
    }

    final void postCurrentItemId(@Nullable Long id) {
        currentId.postValue(id);
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
        return Shift.ShiftType.LiveShiftConfiguration.getInstance(getApplication()).getNewValue(getDefaultSharedPreferences());
    }

    @PluralsRes
    abstract int getQuantityStringResource();

    @NonNull
    @Override
    public final String getTitle(int count) {
        return getApplication().getResources().getQuantityString(getQuantityStringResource(), count, count);
    }

    @NonNull
    @Override
    public final LiveData<List<FinalItem>> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public final void deleteItems() {
        List<FinalItem> items = getAllItems().getValue();
        SparseBooleanArray positions = selectedPositions.getValue();
        if (items == null || positions == null) return;
        final Set<Long> itemIds = new HashSet<>();
        for (int i = 0; i < positions.size(); i++) {
            if (positions.valueAt(i)) {
                itemIds.add(items.get(positions.keyAt(i)).getId());
            }
        }
        if (itemIds.isEmpty()) return;
        positions.clear();
        selectedPositions.setValue(positions);
        runAsync(new Runnable() {
            @Override
            public void run() {
                deletedItemRestorer.postValue(new DeletedItemsRestorer(getDao().deleteAndReturnDeletedItemsSync(itemIds), getQuantityStringResource()));
            }
        });
    }

    private final class DeletedItemsRestorer implements DeletedItemsInfo {

        @NonNull
        private final String message;

        @NonNull
        private final List<Entity> deletedItems;

        DeletedItemsRestorer(@NonNull List<Entity> deletedItems, @PluralsRes int quantityStringResource) {
            this.deletedItems = deletedItems;
            message = getApplication().getString(R.string.items_deleted, getApplication().getResources().getQuantityString(quantityStringResource, deletedItems.size(), deletedItems.size()));
        }

        @Override
        public void onClick(View v) {
            runAsync(new Runnable() {
                @Override
                public void run() {
                    deletedItemRestorer.postValue(null);
                    try {
                        getDao().insertItemsSync(deletedItems);
                    } catch (SQLiteConstraintException e) {
                        postOverlappingShifts();
                    }
                }
            });
        }

        @NonNull
        @Override
        public String getMessage() {
            return message;
        }

    }

}
