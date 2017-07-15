package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.ItemCallbacks;
import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.util.ShiftType;

import java.util.List;

abstract class ShiftAddListFragment<ItemType extends PayableItem, Entity extends ItemType, ViewModel extends ItemCallbacks<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    private FloatingActionMenu mFabMenu;

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFabAdd().hide(true);
        mFabMenu = callbacks.getFloatingActionMenu();
        mFabMenu.hideMenu(true);
        callbacks.getFabNormalDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewShift(ShiftType.NORMAL_DAY);
            }
        });
        callbacks.getFabLongDay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewShift(ShiftType.LONG_DAY);
            }
        });
        callbacks.getFabNightShift().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewShift(ShiftType.NIGHT_SHIFT);
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

    abstract void addNewShift(@NonNull ShiftType shiftType);

}

