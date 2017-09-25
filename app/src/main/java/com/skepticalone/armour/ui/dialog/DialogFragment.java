package com.skepticalone.armour.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

abstract class DialogFragment<Entity> extends AppCompatDialogFragment {

    @NonNull
    abstract ItemViewModelContract<Entity> getViewModel();

    abstract void onUpdateView(@NonNull Entity item);

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

}
