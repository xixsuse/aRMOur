package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

import static com.skepticalone.mecachecker.ui.Constants.ITEM_TYPE_ADDITIONAL_SHIFT;

public final class AdditionalShiftListFragment
        extends ShiftListFragment<AdditionalShift, AdditionalShiftEntity, AdditionalShiftViewModel> {

    @Override
    int getItemType() {
        return ITEM_TYPE_ADDITIONAL_SHIFT;
    }

    @NonNull
    @Override
    ItemListAdapter<AdditionalShift> createAdapter(Context context) {
        return new AdditionalShiftListAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(AdditionalShiftViewModel.class);
    }

}
