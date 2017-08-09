package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.util.ShiftUtil;

public final class AdditionalShiftTotalsDialogFragment extends PayableTotalsDialogFragment<AdditionalShiftEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<AdditionalShiftEntity> createAdapter(@NonNull Context context) {
        return new AdditionalShiftTotalsAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
