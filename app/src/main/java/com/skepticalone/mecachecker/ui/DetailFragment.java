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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;

abstract class DetailFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends LifecycleFragment {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";
    private final ItemDetailAdapter<ItemType> mAdapter = onCreateAdapter();
    private final IntentFilter mErrorIntentFilter = new IntentFilter(ItemViewModel.DISPLAY_ERROR);
    private SnackbarCallbacks mSnackbarCallbacks;
    private final BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSnackbarCallbacks.showSnackbar(intent.getStringExtra(Intent.EXTRA_TEXT), "Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Callbacks", "onClick: cone");
                }
            });
        }
    };
    private ViewModel mModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSnackbarCallbacks = (SnackbarCallbacks) context;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.detail_recycler, container, false);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
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

    void showDialogFragment(AppCompatDialogFragment dialogFragment) {
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }
}
