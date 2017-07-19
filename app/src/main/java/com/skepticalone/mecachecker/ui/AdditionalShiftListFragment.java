package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftListAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

import static com.skepticalone.mecachecker.ui.Constants.ITEM_TYPE_ADDITIONAL_SHIFT;

public final class AdditionalShiftListFragment
        extends ShiftListFragment<AdditionalShift, RosteredShiftEntity, RosteredShiftViewModel> {

    @Override
    int getItemType() {
        return ITEM_TYPE_ADDITIONAL_SHIFT;
    }

    @NonNull
    @Override
    ItemListAdapter<AdditionalShift> createAdapter(Context context) {
        return new RosteredShiftListAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

}
