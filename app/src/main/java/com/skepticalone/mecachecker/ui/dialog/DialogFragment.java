package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.ui.common.LifecycleDialogFragment;

abstract class DialogFragment<Entity> extends LifecycleDialogFragment implements Observer<Entity> {

//    private Callbacks<Entity> callbacks;
//
//    @Override
//    @CallSuper
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        //noinspection unchecked
//        callbacks = (Callbacks<Entity>) getTargetFragment();
//    }

    @NonNull
    abstract ItemViewModelContract<Entity> getViewModel();

//    @NonNull
//    Entity getCurrentItem() {
//        Entity currentItem = getViewModel().getCurrentItem().getValue();
//        if (currentItem == null) throw new IllegalStateException();
//        return currentItem;
//    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getCurrentItem().observe(this, this);
    }
//
//    public interface Callbacks<Entity> {
//        @NonNull ItemViewModelContract<Entity> getViewModel();
//    }

}
