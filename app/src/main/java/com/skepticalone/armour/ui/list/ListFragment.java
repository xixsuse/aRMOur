package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemListAdapter;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.ui.common.BaseFragment;
import com.skepticalone.armour.ui.totals.TotalsDialogFragment;

public abstract class ListFragment<Entity extends Item> extends BaseFragment<Entity> implements ItemListAdapter.Callbacks {
    private static final String TAG = "ListFragment";
    private Callbacks callbacks;
    @NonNull
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        private static final String TAG = "ActionMode.Callback";

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Log.d(TAG, "onCreateActionMode() called with: mode = [" + mode + "], menu = [" + menu + "]");
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    getViewModel().deleteSelectedItems();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Log.d(TAG, "onDestroyActionMode() called with: mode = [" + mode + "]");
            getAdapter().notifyIdsChanged(getViewModel().getSelectedIds());
            getViewModel().getSelectedIds().clear();
            callbacks.setContextualActionMode(null);
        }

    };
    private RecyclerView.LayoutManager mLayoutManager;

    public static ListFragment getNewListFragment(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftListFragment();
        if (itemType == R.id.additional) return new AdditionalShiftListFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverListFragment();
        if (itemType == R.id.expenses) return new ExpenseListFragment();
        throw new IllegalStateException();
    }

    abstract void setupFab(FabCallbacks callbacks);

    abstract void hideFab(FabCallbacks callbacks);

    abstract void showFab(FabCallbacks callbacks);

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        callbacks = (Callbacks) context;
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu);
    }

    @NonNull
    abstract TotalsDialogFragment<Entity> createSummaryDialogFragment();

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.totals) {
            showDialogFragment(createSummaryDialogFragment());
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    protected final int getLayout() {
        return R.layout.list_recycler;
    }

    @NonNull
    @Override
    public final RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = super.onCreateView(inflater, container, savedInstanceState);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideFab(callbacks);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFab(callbacks);
                }
            }
        });
        return recyclerView;
    }

    @Override
    public void scrollToPosition(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemViewModelContract<Entity> viewModel = getViewModel();
        viewModel.getItems().observe(this, getAdapter());
        viewModel.getDeletedItemRestorer().observe(this, new Observer<View.OnClickListener>() {
            @Override
            public void onChanged(@Nullable View.OnClickListener deletedItemRestorer) {
                if (deletedItemRestorer != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    callbacks.onItemRemoved(getItemType(), deletedItemRestorer);
                }
            }
        });
        setupFab(callbacks);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getViewModel().getSelectedIds().isEmpty()) {
            if (callbacks.getContextualActionMode() != null)
                callbacks.getContextualActionMode().finish();
        } else if (callbacks.getContextualActionMode() == null) {
            callbacks.setContextualActionMode(getActivity().startActionMode(mActionModeCallback));
        }
    }

    @NonNull
    @Override
    protected abstract ItemListAdapter<Entity> getAdapter();

    @IdRes
    abstract int getItemType();

    @Override
    public final boolean onClick(long itemId) {
        Log.d(TAG, "onClick() called with: itemId = [" + itemId + "]");
        if (callbacks.getContextualActionMode() == null) {
            getViewModel().selectItem(itemId);
            callbacks.onClick(getItemType(), itemId);
            return false;
        } else {
            getViewModel().toggleSelected(itemId);
            if (getViewModel().getSelectedIds().isEmpty()) {
                callbacks.getContextualActionMode().finish();
            }
            return true;
        }
    }

    @Override
    public final boolean onLongClick(long itemId) {
        Log.d(TAG, "onLongClick() called with: itemId = [" + itemId + "]");
        if (callbacks.getContextualActionMode() == null) {
            getViewModel().toggleSelected(itemId);
            callbacks.setContextualActionMode(getActivity().startActionMode(mActionModeCallback));
            return true;
        } else return false;
//        getViewModel().deleteItem(itemId);
    }

    @Override
    public boolean isSelected(long itemId) {
        return getViewModel().getSelectedIds().contains(itemId);
    }

    public interface Callbacks extends FabCallbacks {
        void onClick(@IdRes int itemType, long itemId);
        void onItemRemoved(@IdRes int itemType, @NonNull View.OnClickListener listener);

        @Nullable
        ActionMode getContextualActionMode();

        void setContextualActionMode(@Nullable ActionMode actionMode);
    }

}
