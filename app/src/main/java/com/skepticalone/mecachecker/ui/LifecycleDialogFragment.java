package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v7.app.AppCompatDialogFragment;


abstract class LifecycleDialogFragment extends AppCompatDialogFragment implements LifecycleRegistryOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public final LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

}
