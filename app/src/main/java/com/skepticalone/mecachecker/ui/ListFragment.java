package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.ui.adapter.ItemListAdapter;

import java.util.List;


abstract class ListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends LifecycleFragment implements ItemListAdapter.Callbacks {

    private final ItemListAdapter<ItemType> mAdapter = onCreateAdapter();
    private ViewModel mModel;
    private Callbacks mCallbacks;
    private RecyclerView.LayoutManager mLayoutManager;
    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {

        private static final String TAG = "mObserver";

        @Override
        public void onChanged() {
            Log.d(TAG, "onChanged() called");
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "], payload = [" + payload + "]");
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            Log.d(TAG, "onItemRangeRemoved() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            Log.d(TAG, "onItemRangeMoved() called with: fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "], itemCount = [" + itemCount + "]");
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            Log.d(TAG, "onItemRangeInserted() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            Log.d(TAG, "onItemRangeInserted: scrolling to position: " + (positionStart + itemCount - 1));
            mLayoutManager.scrollToPosition(positionStart + itemCount - 1);
        }

    };

    @NonNull
    abstract ItemListAdapter<ItemType> onCreateAdapter();

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_list, container, false);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    abstract void setupFab(FloatingActionMenu menu, FloatingActionButton fabNormalDay, FloatingActionButton fabLongDay, FloatingActionButton fabNightShift);

    abstract Class<ViewModel> getViewModelClass();

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = ViewModelProviders.of(getActivity()).get(getViewModelClass());
        mModel.getItems().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(@Nullable List<Entity> items) {
                mAdapter.setItems(items);
            }
        });
        setupFab(
                mCallbacks.getFloatingActionMenu(),
                mCallbacks.getFabNormalDay(),
                mCallbacks.getFabLongDay(),
                mCallbacks.getFabNightShift()
        );
    }

    final ItemViewModel<Entity> getViewModel() {
        return mModel;
    }

    @Override
    public final void onStart() {
        super.onStart();
        mAdapter.registerAdapterDataObserver(mObserver);
    }

    @Override
    public final void onStop() {
        super.onStop();
        mAdapter.unregisterAdapterDataObserver(mObserver);
    }

    abstract int getItemType();

    @Override
    public void onClick(long itemId) {
        if (!mCallbacks.onItemSelected(getItemType(), itemId)) {
            mModel.selectItem(itemId);
        }
    }

    @Override
    public final void onLongClick(long itemId) {
        mModel.deleteItem(itemId);
    }

    interface Callbacks {
        // returns true if activity handled the event
        boolean onItemSelected(int itemType, long itemId);

        FloatingActionMenu getFloatingActionMenu();

        FloatingActionButton getFabNormalDay();

        FloatingActionButton getFabLongDay();

        FloatingActionButton getFabNightShift();
    }
}
