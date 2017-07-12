package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;

abstract class DetailFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends LifecycleFragment {

    static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";
    private final ItemDetailAdapter<ItemType> mAdapter = onCreateAdapter();
    private final IntentFilter mErrorIntentFilter = new IntentFilter(ItemViewModel.DISPLAY_ERROR);
    private ViewModel mModel;
    private CoordinatorLayout mCoordinatorLayout;
    private final BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Snackbar.make(mCoordinatorLayout, intent.getStringExtra(Intent.EXTRA_TEXT), Snackbar.LENGTH_LONG).show();
        }
    };

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCoordinatorLayout = (CoordinatorLayout) inflater.inflate(R.layout.detail_fragment, container, false);
        RecyclerView recyclerView = mCoordinatorLayout.findViewById(R.id.recycler);
        recyclerView.setAdapter(mAdapter);
        return mCoordinatorLayout;
    }

    @NonNull
    abstract ItemDetailAdapter<ItemType> onCreateAdapter();

    abstract Class<ViewModel> getViewModelClass();

    final ViewModel getViewModel() {
        return mModel;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = ViewModelProviders.of(getActivity()).get(getViewModelClass());
        mModel.getSelectedItem().observe(this, new Observer<Entity>() {
                    @Override
                    public void onChanged(@Nullable Entity entity) {
                        if (entity != null) mAdapter.setItem(entity);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mErrorReceiver, mErrorIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mErrorReceiver);
    }

}
