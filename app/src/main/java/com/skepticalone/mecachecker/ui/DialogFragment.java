package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

abstract class DialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends LifecycleDialogFragment implements Observer<Entity> {

    private ViewModel viewModel;

    @Nullable
    private Entity currentItem;

    @NonNull
    abstract ViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);

    @Override
    @CallSuper
    public void onChanged(@Nullable Entity item) {
        currentItem = item;
    }

    @NonNull
    Entity getCurrentItem() {
        if (currentItem == null) throw new IllegalStateException();
        return currentItem;
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = onCreateViewModel(ViewModelProviders.of(getActivity()));
        viewModel.getCurrentItem().observe(this, this);
    }

    final ViewModel getViewModel() {
        return viewModel;
    }

}
