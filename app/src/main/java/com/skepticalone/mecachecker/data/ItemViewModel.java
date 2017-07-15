package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.ui.Constants;

import java.util.List;

public abstract class ItemViewModel<Entity extends Item> extends AndroidViewModel implements BaseItemViewModel<Entity> {

    private static final MutableLiveData NO_ITEM = new MutableLiveData<>();
    private static final MutableLiveData<List> NO_ITEMS = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final LiveData<Entity> selectedItem;

    ItemViewModel(Application application) {
        super(application);
        selectedItem = Transformations.switchMap(selectedId, new Function<Long, LiveData<Entity>>() {
            @Override
            public LiveData<Entity> apply(Long id) {
                //noinspection unchecked
                return id == null ? NO_ITEM : getItem(id);
            }
        });
    }

    @Override
    public final LiveData<Entity> getSelectedItem() {
        return selectedItem;
    }

    @Override
    public final void selectItem(long id) {
        selectedId.setValue(id);
    }

    final void runAsync(SQLiteTask task) {
        new DatabaseOperation(getApplication()).execute(task);
    }

    private static final class DatabaseOperation extends AsyncTask<SQLiteTask, Void, Void> {

        private final LocalBroadcastManager mBroadcastManager;

        DatabaseOperation(Application application) {
            super();
            mBroadcastManager = LocalBroadcastManager.getInstance(application);
        }

        @Override
        protected final Void doInBackground(SQLiteTask[] tasks) {
            for (SQLiteTask task : tasks) {
                try {
                    task.runSQLiteTask();
                } catch (ShiftOverlapException e) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.DISPLAY_ERROR);
                    intent.putExtra(Intent.EXTRA_TEXT, e.getMessage());
                    mBroadcastManager.sendBroadcast(intent);
                }
            }
            return null;
        }

    }

}
