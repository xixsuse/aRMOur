package com.skepticalone.mecachecker.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.SingleAddItemViewModelContract;

abstract class SingleAddListFragment<Entity extends Item> extends ListFragment<Entity> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        FloatingActionMenu fabMenu = callbacks.getFloatingActionMenu();
        fabMenu.close(true);
        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewItem();
            }
        });
    }

    @NonNull
    @Override
    protected abstract SingleAddItemViewModelContract<Entity> getViewModel();

}

