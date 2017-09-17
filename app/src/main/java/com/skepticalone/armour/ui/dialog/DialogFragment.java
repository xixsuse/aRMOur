package com.skepticalone.armour.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

abstract class DialogFragment<Entity> extends AppCompatDialogFragment {

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

    abstract void onUpdateView(@NonNull Entity item);

//    @NonNull
//    Entity getCurrentItem() {
//        Entity currentItem = getViewModel().getCurrentItem().getValue();
//        if (currentItem == null) throw new IllegalStateException();
//        return currentItem;
//    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Entity item = getViewModel().getCurrentItem().getValue();
            if (item != null) {
                onUpdateView(item);
            }
        }
    }
//
//    public interface Callbacks<Entity> {
//        @NonNull ItemViewModelContract<Entity> getViewModel();
//    }

}
