package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public abstract class DialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends AppCompatDialogFragment implements Observer<Entity>, LifecycleRegistryOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ViewModel viewModel;

    @Nullable
    private Entity currentItem;

    @NonNull
    abstract ViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);

    @Override
    public final LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

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
