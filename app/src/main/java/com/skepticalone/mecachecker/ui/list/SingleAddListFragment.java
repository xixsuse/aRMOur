package com.skepticalone.mecachecker.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.SingleAddItemViewModelContract;

abstract class SingleAddListFragment<Entity extends Item> extends ListFragment<Entity> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFabNormalDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewItem();
            }
        });
        callbacks.getFabNormalDay().show();
        callbacks.getFabLongDay().hide();
        callbacks.getFabNightShift().hide();
    }

    @NonNull
    @Override
    protected abstract SingleAddItemViewModelContract<Entity> getViewModel();

}

