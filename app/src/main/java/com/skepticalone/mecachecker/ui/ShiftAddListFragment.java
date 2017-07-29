package com.skepticalone.mecachecker.ui;

import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ShiftViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftAddListFragment<Entity extends Item, ViewModel extends ShiftViewModelContract<Entity>> extends ListFragment<Entity, ViewModel> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
        final FloatingActionMenu fabMenu = callbacks.getFloatingActionMenu();
        fabMenu.close(true);
        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.toggle(true);
            }
        });
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

}

