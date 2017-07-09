package com.skepticalone.mecachecker.ui;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;

abstract class SingleAddListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    @Override
    final void setupFab(FloatingActionMenu menu, FloatingActionButton fabNormalDay, FloatingActionButton fabLongDay, FloatingActionButton fabNightShift) {
        menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    final void addItem() {
        getViewModel().addRandomItem();
    }

}

