package com.skepticalone.mecachecker.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.SingleAddItemViewModelContract;

abstract class SingleAddListFragment<Entity extends Item> extends ListFragment<Entity> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFabLongDay().hide();
        callbacks.getFabNightShift().hide();
        callbacks.getFabNormalDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewItem();
            }
        });
        callbacks.getFabNormalDay().show();
    }

    @Override
    final void hideFab(FabCallbacks callbacks) {
        callbacks.getFabNormalDay().hide();
    }

    @Override
    final void showFab(FabCallbacks callbacks) {
        callbacks.getFabNormalDay().show();
    }

    @NonNull
    @Override
    protected abstract SingleAddItemViewModelContract<Entity> getViewModel();

}

