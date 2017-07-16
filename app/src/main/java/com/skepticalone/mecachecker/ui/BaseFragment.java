package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


abstract class BaseFragment<Adapter extends RecyclerView.Adapter, ViewModel> extends LifecycleFragment {

    private Adapter mAdapter;
    private ViewModel viewModel;
    @NonNull
    abstract Adapter onCreateAdapter(Context context);
    @NonNull
    abstract ViewModel onCreateViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = onCreateAdapter(context);
    }

    @NonNull
    @Override
    @CallSuper
    public RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(getLayout(), container, false);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    @CallSuper
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = onCreateViewModel();
    }

    @NonNull
    final ViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    final Adapter getAdapter() {
        return mAdapter;
    }

    @LayoutRes
    abstract int getLayout();

}
