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

abstract class IndependentDialogFragment<Entity, ViewModel extends BaseViewModel<Entity>> extends AppCompatDialogFragment implements LifecycleRegistryOwner {
//
//    private static final String VIEW_MODEL_CLASS = "VIEW_MODEL_CLASS";
//
//    static Bundle getArgs(@NonNull Class<? extends BaseViewModel> viewModelClass) {
//        Bundle args = new Bundle();
//        args.putSerializable(VIEW_MODEL_CLASS, viewModelClass);
//        return args;
//    }

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ViewModel viewModel;

    @NonNull
    abstract ViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);
// {
//        //noinspection unchecked
//        Class<android.arch.lifecycle.ViewModel> viewModelClass = (Class<android.arch.lifecycle.ViewModel>) getArguments().getSerializable(VIEW_MODEL_CLASS);
//        assert viewModelClass != null;
//        //noinspection unchecked
//        return (ViewModel) ViewModelProviders.of(activity).get(viewModelClass);
//    }

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
