package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.model.AdditionalShift;
import com.skepticalone.mecachecker.util.ShiftUtil;

import static com.skepticalone.mecachecker.ui.Constants.ITEM_TYPE_ADDITIONAL_SHIFT;

public final class AdditionalShiftListFragment
        extends ShiftListFragment<AdditionalShift, AdditionalShiftEntity> {

    private AdditionalShiftViewModel model;
    private ShiftUtil.Calculator calculator;

    @NonNull
    @Override
    ItemListAdapter<AdditionalShift> onCreateAdapter(Context context) {
        calculator = new ShiftUtil.Calculator(context);
        return new AdditionalShiftListAdapter(this, calculator);
    }

    @NonNull
    @Override
    Model<AdditionalShiftEntity> onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
        return model;
    }

    @Override
    void addNewShift(@NonNull ShiftUtil.ShiftType shiftType) {
        model.addNewShift(calculator.getStartTime(shiftType), calculator.getEndTime(shiftType));
    }

    @Override
    int getItemType() {
        return ITEM_TYPE_ADDITIONAL_SHIFT;
    }

}
