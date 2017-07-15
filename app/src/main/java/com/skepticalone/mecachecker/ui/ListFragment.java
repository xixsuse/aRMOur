package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.Model;
import com.skepticalone.mecachecker.model.Item;

import java.util.List;

abstract class ListFragment<ItemType extends Item, ViewModel extends Model<ItemType>> extends LifecycleFragment implements ItemListAdapter.Callbacks, Observer<List<ItemType>> {

    final static String IS_TWO_PANE = "IS_TWO_PANE";
    private final ItemListAdapter<ItemType> mAdapter = createAdapter();
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
    abstract ItemListAdapter<ItemType> createAdapter();

    @NonNull
    abstract ViewModel createViewModel();

    final ViewModel getViewModel() {
        return mModel;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.list_recycler, container, false);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    abstract void setupFab(FabCallbacks callbacks);

    @Override
    @CallSuper
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = createViewModel();
        mModel.getItems().observe(this, this);
        if (getArguments().getBoolean(IS_TWO_PANE, false)) {
            mModel.getSelectedItem().observe(this, new Observer<ItemType>() {
                @Override
                public void onChanged(@Nullable ItemType entity) {
                    mAdapter.setSelectedId(entity == null ? -1 : entity.getId());
                }
            });
        }
        setupFab(mCallbacks);
    }

    @Override
    @CallSuper
    public void onChanged(@Nullable List<ItemType> entities) {
        mAdapter.setItems(entities);
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
        mModel.selectItem(itemId);
        mCallbacks.onItemSelected(getItemType(), itemId);
    }

    @Override
    public final void onLongClick(long itemId) {
        mModel.deleteItem(itemId);
    }

    interface FabCallbacks {
        FloatingActionMenu getFloatingActionMenu();

        FloatingActionButton getFabNormalDay();

        FloatingActionButton getFabLongDay();

        FloatingActionButton getFabNightShift();

        FloatingActionButton getFabAdd();
    }

    interface Callbacks extends FabCallbacks {
        void onItemSelected(int itemType, long itemId);
    }
}
