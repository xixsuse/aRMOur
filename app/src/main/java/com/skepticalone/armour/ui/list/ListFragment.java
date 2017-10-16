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
import android.util.SparseBooleanArray;
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
import com.skepticalone.armour.ui.common.BaseFragment;
import com.skepticalone.armour.ui.totals.ItemTotalsDialogFragment;

public abstract class ListFragment<FinalItem extends Item> extends BaseFragment<FinalItem> implements ItemListAdapter.Callbacks, ActionMode.Callback {

    private Callbacks callbacks;

    private RecyclerView recyclerView;

    @Nullable
    private ActionMode mSelectMode;

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
    public final void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
        setHasOptionsMenu(true);
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    @NonNull
    abstract ItemTotalsDialogFragment<FinalItem> createSummaryDialogFragment(boolean subtotals);

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.totals) {
            showDialogFragment(createSummaryDialogFragment(false));
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
                if (mSelectMode == null) {
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
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab(callbacks);
        getViewModel().getAllItems().observe(this, getAdapter());
        getViewModel().getDeletedItemsInfo().observe(this, new Observer<DeletedItemsInfo>() {
            @Override
            public void onChanged(@Nullable DeletedItemsInfo deletedItemsInfo) {
                if (deletedItemsInfo != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    callbacks.onItemRemoved(deletedItemsInfo);
                }
            }
        });
        getViewModel().getSelectedPositions().observe(this, new Observer<SparseBooleanArray>() {
            @Override
            public void onChanged(@Nullable SparseBooleanArray selectedPositions) {
                if (selectedPositions != null) {
                    int count = selectedPositions.size();
                    if (count > 0) {
                        if (mSelectMode == null) {
                            mSelectMode = getActivity().startActionMode(ListFragment.this);
                        }
                        //noinspection ConstantConditions
                        mSelectMode.setTitle(getViewModel().getTitle(count));
                    } else if (mSelectMode != null) {
                        mSelectMode.finish();
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    protected abstract ItemListAdapter<FinalItem> getAdapter();

    @IdRes
    abstract int getItemType();

    @CallSuper
    void inflateSelectionMenu(MenuInflater inflater, Menu menu) {
        inflater.inflate(R.menu.selection_menu, menu);
    }

    @Override
    public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getViewModel().setCurrentItemId(null);
        hideFab(callbacks);
        callbacks.setNavigationBarVisibility(View.GONE);
        recyclerView.setPadding(0, 0, 0, 0);
        inflateSelectionMenu(mode.getMenuInflater(), menu);
        return true;
    }

    @Override
    public final boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    @CallSuper
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                getViewModel().deleteItems();
                return true;
            case R.id.subtotals:
                showDialogFragment(createSummaryDialogFragment(true));
                return true;
            default:
                return false;
        }
    }

    @Override
    public final void onDestroyActionMode(ActionMode mode) {
        mSelectMode = null;
        SparseBooleanArray selectedPositions = getSelectedPositions();
        if (selectedPositions.size() > 0) {
            for (int i = 0; i < selectedPositions.size(); i++) {
                if (selectedPositions.valueAt(i)) {
                    getAdapter().notifyItemChanged(selectedPositions.keyAt(i));
                }
            }
            selectedPositions.clear();
            getViewModel().setSelectedPositions(selectedPositions);
        }
        recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_item_height));
        callbacks.setNavigationBarVisibility(View.VISIBLE);
        showFab(callbacks);
    }

    @NonNull
    private SparseBooleanArray getSelectedPositions() {
        //noinspection ConstantConditions
        return getViewModel().getSelectedPositions().getValue();
    }

    private void setSelected(int position, boolean selected) {
        SparseBooleanArray selectedPositions = getSelectedPositions();
        if (selected) selectedPositions.put(position, true);
        else selectedPositions.delete(position);
        getViewModel().setSelectedPositions(selectedPositions);
        getAdapter().notifyItemChanged(position);
    }

    @Override
    public boolean showSelectedIcon(int position) {
        return mSelectMode != null && getSelectedPositions().get(position, false);
    }

    @Override
    public void onClick(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (mSelectMode == null) {
            long itemId = viewHolder.getItemId();
            getViewModel().setCurrentItemId(itemId);
            callbacks.onClick(getItemType(), itemId);
        } else {
            int position = viewHolder.getAdapterPosition();
            setSelected(position, !getSelectedPositions().get(position, false));
        }
    }

    @Override
    public boolean onLongClick(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (mSelectMode == null) {
            setSelected(viewHolder.getAdapterPosition(), true);
            return true;
        }
        return false;
    }

    public interface Callbacks extends FabCallbacks {
        void onClick(@IdRes int itemType, long itemId);
        void onItemRemoved(@NonNull DeletedItemsInfo deletedItemsInfo);
        void setNavigationBarVisibility(int visibility);
    }

}
