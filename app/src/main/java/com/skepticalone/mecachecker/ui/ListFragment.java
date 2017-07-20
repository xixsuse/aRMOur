package com.skepticalone.mecachecker.ui;

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
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.EntityObserver;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

import java.util.List;

abstract class ListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends BaseFragment<ItemListAdapter<ItemType>, ViewModel>
        implements ItemListAdapter.Callbacks, Observer<List<Entity>> {

    final static String IS_TWO_PANE = "IS_TWO_PANE";
    private Callbacks mCallbacks;
    private final EntityObserver<Entity> itemDeletedObserver = new EntityObserver<Entity>(){
        @Override
        public void update(@Nullable final Entity deletedItem) {
            if (deletedItem != null) {
                snackbarCallbacks.showSnackbar(R.string.item_removed, R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getViewModel().insertItem(deletedItem);
                    }
                });
            }
        }
    };

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

    abstract void setupFab(FabCallbacks callbacks);

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    final int getLayout() {
        return R.layout.list_recycler;
    }

    @NonNull
    @Override
    public final RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = super.onCreateView(inflater, container, savedInstanceState);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getItems().observe(this, this);
        if (getArguments().getBoolean(IS_TWO_PANE, false)) {
            getViewModel().selectedItem.observe(this, new Observer<Entity>() {
                @Override
                public void onChanged(@Nullable Entity entity) {
                    getAdapter().setSelectedId(entity == null ? -1 : entity.getId());
                }
            });
        }
        setupFab(mCallbacks);
    }

    @Override
    @CallSuper
    public void onChanged(@Nullable List<Entity> entities) {
        getAdapter().setItems(entities);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAdapter().registerAdapterDataObserver(mObserver);
        getViewModel().lastDeletedItem.addObserver(itemDeletedObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getViewModel().lastDeletedItem.deleteObserver(itemDeletedObserver);
        getAdapter().unregisterAdapterDataObserver(mObserver);
    }

    abstract int getItemType();

    @Override
    public final void onClick(long itemId) {
        getViewModel().selectItem(itemId);
        mCallbacks.onItemSelected(getItemType(), itemId);
    }

    @Override
    public final void onLongClick(long itemId) {
        getViewModel().deleteItem(itemId);
    }

    interface FabCallbacks {
        FloatingActionMenu getFloatingActionMenu();

        FloatingActionButton getFabNormalDay();

        FloatingActionButton getFabLongDay();

        FloatingActionButton getFabNightShift();
    }

    interface Callbacks extends FabCallbacks {
        void onItemSelected(int itemType, long itemId);
    }
}
