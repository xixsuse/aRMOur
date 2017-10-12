package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
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

public abstract class ListFragment<FinalItem extends Item> extends BaseFragment<FinalItem> implements ItemListAdapter.Callbacks, ActionMode.Callback {
    private Callbacks callbacks;

    private RecyclerView recyclerView;
    @Nullable
    private volatile ActionMode mActionMode;

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
    abstract TotalsDialogFragment<FinalItem> createSummaryDialogFragment();

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
        recyclerView = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mActionMode == null) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        hideFab(callbacks);
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showFab(callbacks);
                    }
                }
            }
        });
        return recyclerView;
    }

    @Override
    public void scrollToPosition(int position) {
        recyclerView.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemViewModelContract<FinalItem> viewModel = getViewModel();
        viewModel.getItems().observe(this, getAdapter());
        viewModel.getDeletedItemsInfo().observe(this, new Observer<DeletedItemsInfo>() {
            @Override
            public void onChanged(@Nullable DeletedItemsInfo deletedItemsInfo) {
                if (deletedItemsInfo != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    callbacks.onItemRemoved(deletedItemsInfo);
                }
            }
        });
        setupFab(callbacks);
    }

    @NonNull
    @Override
    protected abstract ItemListAdapter<FinalItem> getAdapter();

    @IdRes
    abstract int getItemType();

    @Override
    public final boolean onClick(long itemId) {
        if (mActionMode == null) {
            getViewModel().selectItem(itemId);
            callbacks.onClick(getItemType(), itemId);
            return false;
        } else {
            getViewModel().toggleSelected(itemId);
            //noinspection ConstantConditions
            setActionModeTitle(mActionMode);
            return true;
        }
    }

    @Override
    public final boolean onLongClick(long itemId) {
        if (mActionMode == null) {
            getViewModel().getSelectedIds().add(itemId);
            mActionMode = getActivity().startActionMode(this);
            return true;
        } else return false;
    }

    @Override
    public final boolean isSelected(long itemId) {
        return getViewModel().getSelectedIds().contains(itemId);
    }

    @Override
    public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
        hideFab(callbacks);
        callbacks.setNavigationBarVisibility(View.GONE);
        recyclerView.setPadding(0, 0, 0, 0);
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.contextual_action_menu, menu);
        setActionModeTitle(mode);
        return true;
    }

    private void setActionModeTitle(@NonNull ActionMode actionMode) {
        int size = getViewModel().getSelectedIds().size();
        actionMode.setTitle(getResources().getQuantityString(getQuantityStringResource(), size, size));
    }

    @PluralsRes
    abstract int getQuantityStringResource();

    @Override
    public final boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public final boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                getViewModel().deleteSelectedItems(getQuantityStringResource());
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public final void onDestroyActionMode(ActionMode mode) {
        showFab(callbacks);
        callbacks.setNavigationBarVisibility(View.VISIBLE);
        if (!getViewModel().getSelectedIds().isEmpty()) {
            getAdapter().notifyIdsChanged(getViewModel().getSelectedIds());
            getViewModel().getSelectedIds().clear();
        }
        recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_item_height));
        mActionMode = null;
    }

    public interface Callbacks extends FabCallbacks {
        void onClick(@IdRes int itemType, long itemId);
        void onItemRemoved(@NonNull DeletedItemsInfo deletedItemsInfo);
        void setNavigationBarVisibility(int visibility);
    }

}
