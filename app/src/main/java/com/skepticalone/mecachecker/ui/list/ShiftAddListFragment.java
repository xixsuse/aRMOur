package com.skepticalone.mecachecker.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ShiftViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftAddListFragment<Entity extends Item> extends ListFragment<Entity> {

    @Override
    final void setupFab(FabCallbacks callbacks) {
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
        callbacks.getFabNormalDay().show();
        callbacks.getFabLongDay().show();
        callbacks.getFabNightShift().show();
    }

    @Override
    final void hideFab(FabCallbacks callbacks) {
        callbacks.getFabNormalDay().hide();
        callbacks.getFabLongDay().hide();
        callbacks.getFabNightShift().hide();
    }

    @Override
    final void showFab(FabCallbacks callbacks) {
        callbacks.getFabNormalDay().show();
        callbacks.getFabLongDay().show();
        callbacks.getFabNightShift().show();
    }

    @NonNull
    @Override
    protected abstract ShiftViewModelContract<Entity> getViewModel();

}

