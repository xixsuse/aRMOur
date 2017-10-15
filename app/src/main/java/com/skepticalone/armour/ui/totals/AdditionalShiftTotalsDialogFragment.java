package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftTotalsDialogFragment extends PayableTotalsDialogFragment<AdditionalShift> {

    public static AdditionalShiftTotalsDialogFragment newInstance(boolean selected) {
        Bundle args = new Bundle();
        args.putBoolean(SELECTED, selected);
        AdditionalShiftTotalsDialogFragment fragment = new AdditionalShiftTotalsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ItemTotalsAdapter<AdditionalShift> createAdapter(@NonNull Context context) {
        return new AdditionalShiftTotalsAdapter(context, isSelected() ? R.string.selected_additional_shifts : R.string.all_additional_shifts, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShift> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
