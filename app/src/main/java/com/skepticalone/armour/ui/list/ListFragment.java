package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.skepticalone.armour.ui.common.BaseFragment;
import com.skepticalone.armour.ui.totals.TotalsDialogFragment;

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
        getViewModel().getItems().observe(this, getAdapter());
        getViewModel().getDeletedItemsInfo().observe(this, new Observer<DeletedItemsInfo>() {
            @Override
            public void onChanged(@Nullable DeletedItemsInfo deletedItemsInfo) {
                if (deletedItemsInfo != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    callbacks.onItemRemoved(deletedItemsInfo);
                }
            }
        });
        if (getViewModel().getSelectedPositions().size() > 0) {
            startActionMode();
        }
    }

    @NonNull
    @Override
    protected abstract ItemListAdapter<FinalItem> getAdapter();

    @IdRes
    abstract int getItemType();

    @Override
    public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mSelectMode = mode;
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.contextual_action_menu, menu);
        return true;
    }

    @Override
    public final boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        int size = getViewModel().getSelectedPositions().size();
        if (size == 0) {
            mode.finish();
            return false;
        } else {
            mode.setTitle(getViewModel().getTitle(size));
            return true;
        }
    }

    @Override
    public final boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                getViewModel().deleteItems(getAdapter());
                mSelectMode = null;
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public final void onDestroyActionMode(ActionMode mode) {
        if (mSelectMode != null) {
            for (int i = 0; i < getViewModel().getSelectedPositions().size(); i++) {
                if (getViewModel().getSelectedPositions().valueAt(i)) {
                    getAdapter().notifyItemChanged(getViewModel().getSelectedPositions().keyAt(i));
                }
            }
            mSelectMode = null;
        }
        getViewModel().getSelectedPositions().clear();
        recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_item_height));
        callbacks.setNavigationBarVisibility(View.VISIBLE);
        showFab(callbacks);
    }

    private void setSelected(int position, boolean selected) {
        if (selected) getViewModel().getSelectedPositions().put(position, true);
        else getViewModel().getSelectedPositions().delete(position);
        getAdapter().notifyItemChanged(position);
    }

    @Override
    public boolean showSelectedIcon(int position) {
        return mSelectMode != null && getViewModel().getSelectedPositions().get(position, false);
    }

    @Override
    public void onClick(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (mSelectMode == null) {
            long itemId = viewHolder.getItemId();
            getViewModel().setCurrentItemId(itemId);
            callbacks.onClick(getItemType(), itemId);
        } else {
            int position = viewHolder.getAdapterPosition();
            setSelected(position, !getViewModel().getSelectedPositions().get(position, false));
            mSelectMode.invalidate();
        }
    }

    @Override
    public boolean onLongClick(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (mSelectMode == null) {
            getViewModel().getSelectedPositions().clear();
            setSelected(viewHolder.getAdapterPosition(), true);
            startActionMode();
            return true;
        }
        return false;
    }

    private void startActionMode() {
        getViewModel().setCurrentItemId(null);
        hideFab(callbacks);
        callbacks.setNavigationBarVisibility(View.GONE);
        recyclerView.setPadding(0, 0, 0, 0);
        getActivity().startActionMode(this);
    }

    public interface Callbacks extends FabCallbacks {
        void onClick(@IdRes int itemType, long itemId);
        void onItemRemoved(@NonNull DeletedItemsInfo deletedItemsInfo);
        void setNavigationBarVisibility(int visibility);
    }

}
