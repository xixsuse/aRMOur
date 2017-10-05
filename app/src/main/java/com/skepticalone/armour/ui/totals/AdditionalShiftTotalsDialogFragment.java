package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftTotalsDialogFragment extends PayableTotalsDialogFragment<AdditionalShift> {

    @NonNull
    @Override
    ItemTotalsAdapter<AdditionalShift> createAdapter(@NonNull Context context) {
        return new AdditionalShiftTotalsAdapter(context, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShift> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
