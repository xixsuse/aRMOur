package com.skepticalone.mecachecker.ui;

import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.SingleAddViewModelContract;

abstract class SingleAddListFragment<Entity extends Item, ViewModel extends SingleAddViewModelContract<Entity>> extends ListFragment<Entity, ViewModel> {

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

}

