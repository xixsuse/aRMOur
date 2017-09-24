package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftTotalsDialogFragment extends PayableTotalsDialogFragment<RawAdditionalShiftEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<RawAdditionalShiftEntity> createAdapter() {
        return new AdditionalShiftTotalsAdapter(this);
    }

    @NonNull
    @Override
    ItemViewModelContract<RawAdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
