package com.skepticalone.mecachecker.ui.list;

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

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.ui.common.BaseFragment;
import com.skepticalone.mecachecker.ui.totals.TotalsDialogFragment;

public abstract class ListFragment<Entity extends Item> extends BaseFragment<Entity> implements ItemListAdapter.Callbacks {

    private Callbacks callbacks;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ListFragment getNewListFragment(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftListFragment();
        if (itemType == R.id.additional) return new AdditionalShiftListFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverListFragment();
        if (itemType == R.id.expenses) return new ExpenseListFragment();
        throw new IllegalStateException();
    }

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
    protected final int getLayout() {
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

    @NonNull
    @Override
    protected abstract ItemListAdapter<Entity> getAdapter();

    @IdRes
    abstract int getItemType();

    @Override
    public final void onClick(long itemId) {
        getViewModel().selectItem(itemId);
        callbacks.onClick(getItemType(), itemId);
    }

    @Override
    public final void onLongClick(long itemId) {
        getViewModel().deleteItem(itemId);
    }

    public interface Callbacks extends FabCallbacks {
        void onClick(@IdRes int itemType, long itemId);
        void onItemRemoved(@IdRes int itemType, @NonNull View.OnClickListener listener);
    }

}
