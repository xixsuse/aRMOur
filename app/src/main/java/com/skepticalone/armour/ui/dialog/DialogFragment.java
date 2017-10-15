package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

abstract class DialogFragment<FinalItem> extends AppCompatDialogFragment {

    abstract void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);

    @NonNull
    abstract ItemViewModelContract<FinalItem> getViewModel();

    abstract void onUpdateView(@NonNull FinalItem item);

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateViewModel(ViewModelProviders.of(getActivity()));
        if (savedInstanceState == null) {
            FinalItem item = getViewModel().getCurrentItem().getValue();
            if (item != null) {
                onUpdateView(item);
            }
        }
    }

}
