package com.skepticalone.mecachecker.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ShiftAddItemViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

abstract class ShiftListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ShiftAddItemViewModel<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    private FloatingActionMenu mFabMenu;

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFabAdd().hide(true);
        mFabMenu = callbacks.getFloatingActionMenu();
        mFabMenu.hideMenu(true);
        callbacks.getFabNormalDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewShift(ShiftUtil.ShiftType.NORMAL_DAY);
            }
        });
        callbacks.getFabLongDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewShift(ShiftUtil.ShiftType.LONG_DAY);
            }
        });
        callbacks.getFabNightShift().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addNewShift(ShiftUtil.ShiftType.NIGHT_SHIFT);
            }
        });
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        mFabMenu.hideMenu(true);
    }

    @Override
    public final void onChanged(@Nullable List<Entity> entities) {
        super.onChanged(entities);
        if (entities == null) {
            mFabMenu.hideMenu(true);
        } else {
            mFabMenu.showMenu(true);
        }
    }

}

