package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

abstract class BaseFragment<Entity> extends LifecycleFragment {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";

//    private Adapter adapter;
//    private ViewModel viewModel;
//
    @LayoutRes
    abstract int getLayout();

    @NonNull
    abstract RecyclerView.Adapter getAdapter();

    @NonNull
    abstract ItemViewModel<Entity> getViewModel();

    @NonNull
    @Override
    @CallSuper
    public RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(getLayout(), container, false);
        recyclerView.setAdapter(getAdapter());
        return recyclerView;
    }

    final void showDialogFragment(AppCompatDialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }
}
