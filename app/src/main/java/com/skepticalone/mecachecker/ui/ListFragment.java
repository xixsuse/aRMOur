package com.skepticalone.mecachecker.ui;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

import java.util.List;

abstract class ListFragment<Entity extends Item> extends BaseFragment<Entity>
        implements ItemListAdapter.Callbacks, Observer<List<Entity>> {

    private Callbacks callbacks;

    static ListFragment getNewListFragment(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftListFragment();
        if (itemType == R.id.additional) return new AdditionalShiftListFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverListFragment();
        if (itemType == R.id.expenses) return new ExpenseListFragment();
        throw new IllegalStateException();
    }

    private RecyclerView.LayoutManager mLayoutManager;

    abstract void setupFab(FabCallbacks callbacks);

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
    public void scrollToPosition(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemViewModel<Entity> viewModel = getViewModel();
        viewModel.getItems().observe(this, this);
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

    @NonNull
    @Override
    abstract ItemListAdapter<Entity> getAdapter();

    @Override
    @CallSuper
    public void onChanged(@Nullable List<Entity> entities) {
        getAdapter().setItems(entities);
    }

    @IdRes
    abstract int getItemType();

    @Override
    public final void onClick(long itemId) {
        getViewModel().selectItem(itemId);
        callbacks.showDetail(getItemType(), itemId);
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
        void showDetail(int itemType, long itemId);
        void onItemRemoved(@IdRes int itemType, @NonNull View.OnClickListener listener);
    }

}
