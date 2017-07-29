package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public abstract class DialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends AppCompatDialogFragment implements LifecycleRegistryOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ViewModel viewModel;

    @NonNull
    abstract ViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);

    abstract void onCurrentItemChanged(@NonNull Entity item);

    @Override
    public final LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = onCreateViewModel(ViewModelProviders.of(getActivity()));
        if (savedInstanceState == null) {
            viewModel.getCurrentItem().observe(this, new Observer<Entity>() {
                @Override
                public void onChanged(@Nullable Entity item) {
                    if (item != null) onCurrentItemChanged(item);
                }
            });
        }
    }

    final ViewModel getViewModel() {
        return viewModel;
    }

}
