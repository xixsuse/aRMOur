package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;


abstract class BaseFragment<Adapter extends RecyclerView.Adapter, ViewModel extends ItemViewModel> extends LifecycleFragment {

    SnackCallbacks snackbarCallbacks;
    private Adapter adapter;
    private ViewModel viewModel;

    @NonNull
    abstract Adapter createAdapter(Context context);

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        snackbarCallbacks = (SnackCallbacks) context;
        adapter = createAdapter(context);
    }

    @NonNull
    abstract ViewModel createViewModel(ViewModelProvider provider);

    @Override
    @CallSuper
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = createViewModel(ViewModelProviders.of(getActivity()));
    }

    @LayoutRes
    abstract int getLayout();

    @NonNull
    @Override
    @CallSuper
    public RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(getLayout(), container, false);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    final Adapter getAdapter() {
        return adapter;
    }
    public final ViewModel getViewModel() {
        return viewModel;
    }
}
