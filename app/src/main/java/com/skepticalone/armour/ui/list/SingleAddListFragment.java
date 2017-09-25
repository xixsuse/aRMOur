package com.skepticalone.armour.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.viewModel.SingleAddItemViewModelContract;

abstract class SingleAddListFragment<Entity extends Item> extends ListFragment<Entity> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFabLongDay().hide();
        callbacks.getFabNightShift().hide();
        callbacks.getFabPrimary().setImageResource(R.drawable.ic_add_white_24dp);
        callbacks.getFabPrimary().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewItem();
            }
        });
        callbacks.getFabPrimary().show();
    }

    @Override
    final void hideFab(FabCallbacks callbacks) {
        callbacks.getFabPrimary().hide();
    }

    @Override
    final void showFab(FabCallbacks callbacks) {
        callbacks.getFabPrimary().show();
    }

    @NonNull
    @Override
    protected abstract SingleAddItemViewModelContract<Entity> getViewModel();

}

