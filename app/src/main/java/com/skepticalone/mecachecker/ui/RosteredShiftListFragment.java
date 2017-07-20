package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftListAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

import static com.skepticalone.mecachecker.ui.Constants.ITEM_TYPE_ROSTERED_SHIFT;

public final class RosteredShiftListFragment
        extends ShiftListFragment<RosteredShift, RosteredShiftEntity, RosteredShiftViewModel> {

    @Override
    int getItemType() {
        return ITEM_TYPE_ROSTERED_SHIFT;
    }

    @NonNull
    @Override
    ItemListAdapter<RosteredShift> createAdapter(Context context) {
        return new RosteredShiftListAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

}
