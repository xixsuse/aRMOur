package com.skepticalone.mecachecker.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;

import java.util.List;

abstract class SingleAddListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    private FloatingActionMenu mFabMenu;

    @Override
    final void setupFab(FloatingActionMenu menu, FloatingActionButton fabNormalDay, FloatingActionButton fabLongDay, FloatingActionButton fabNightShift) {
        mFabMenu = menu;
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
        mFabMenu.close(true);
    }

    @Override
    public void onChanged(@Nullable List<Entity> entities) {
        super.onChanged(entities);
        if (entities == null) {
            mFabMenu.close(true);
            mFabMenu.hideMenuButton(true);
        } else {
            mFabMenu.close(false);
            mFabMenu.showMenuButton(true);
        }
    }

    final void addItem() {
        getViewModel().addRandomItem();
    }

}

