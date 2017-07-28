package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.util.DeletedItem;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

import java.util.List;

abstract class ListFragment<Entity extends Item, ViewModel extends ViewModelContract<Entity>> extends BaseFragment<ItemListAdapter<Entity>, ViewModel>
        implements ItemListAdapter.Callbacks, Observer<List<Entity>> {

//    final static String IS_TWO_PANE = "IS_TWO_PANE";
    private Callbacks callbacks;
    private final DeletedItem.Observer<Entity> deletedItemObserver = new DeletedItem.Observer<Entity>(){
        @Override
        public void update(@NonNull final Entity deletedItem) {
            callbacks.showSnackbar(R.string.item_removed, R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getViewModel().insertItem(deletedItem);
                }
            });
        }
    };

    static ListFragment getNewListFragment(@IdRes int itemType) {
        if (itemType == R.id.cross_cover) return new CrossCoverListFragment();
        if (itemType == R.id.expenses) return new ExpenseListFragment();
        throw new IllegalStateException();
    }

//    private RecyclerView.LayoutManager mLayoutManager;
//    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
//
//        private static final String TAG = "mObserver";
//
//        @Override
//        public void onChanged() {
//            Log.d(TAG, "onChanged() called");
//        }
//
//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount) {
//            Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
//        }
//
//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
//            Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "], payload = [" + payload + "]");
//        }
//
//        @Override
//        public void onItemRangeRemoved(int positionStart, int itemCount) {
//            Log.d(TAG, "onItemRangeRemoved() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
//        }
//
//        @Override
//        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//            Log.d(TAG, "onItemRangeMoved() called with: fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "], itemCount = [" + itemCount + "]");
//        }
//
//        @Override
//        public void onItemRangeInserted(int positionStart, int itemCount) {
//            Log.d(TAG, "onItemRangeInserted() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
//            Log.d(TAG, "onItemRangeInserted: scrolling to position: " + (positionStart + itemCount - 1));
//            mLayoutManager.scrollToPosition(positionStart + itemCount - 1);
//        }
//
//    };

    abstract void setupFab(FabCallbacks callbacks);

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    final int getLayout() {
        return R.layout.list_recycler;
    }

    @NonNull
    @Override
    public final RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = super.onCreateView(inflater, container, savedInstanceState);
//        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getItems().observe(this, this);
//        if (getArguments().getBoolean(IS_TWO_PANE, false)) {
//            getViewModel().getCurrentItem().observe(this, new Observer<Entity>() {
//                @Override
//                public void onChanged(@Nullable Entity entity) {
//                    getAdapter().setSelectedId(entity == null ? -1 : entity.getId());
//                }
//            });
//        }
        setupFab(callbacks);
    }

    @Override
    @CallSuper
    public void onChanged(@Nullable List<Entity> entities) {
        getAdapter().setItems(entities);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getAdapter().registerAdapterDataObserver(mObserver);
        getViewModel().getDeletedItem().addObserver(deletedItemObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getViewModel().getDeletedItem().deleteObserver(deletedItemObserver);
//        getAdapter().unregisterAdapterDataObserver(mObserver);
    }

    @IdRes
    abstract int getItemType();

    @Override
    public final void onClick(long itemId) {
        getViewModel().selectItem(itemId);
//        callbacks.onItemSelected(getItemType(), itemId);
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
//        void onItemSelected(int itemType, long itemId);
        void showSnackbar(@StringRes int text, @StringRes int action, @NonNull View.OnClickListener listener);
    }

}
