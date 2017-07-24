package com.skepticalone.mecachecker.ui;

import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.BaseViewModel;

abstract class SingleAddListFragment<Entity extends Item, ViewModel extends BaseViewModel<Entity>> extends ListFragment<Entity, ViewModel> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        FloatingActionMenu fabMenu = callbacks.getFloatingActionMenu();
        fabMenu.close(true);
        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });
    }

    abstract void addNewItem();

}

