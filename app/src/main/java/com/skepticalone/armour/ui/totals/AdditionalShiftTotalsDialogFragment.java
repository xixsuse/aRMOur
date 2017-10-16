package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.armour.adapter.FilteredItemTotalsAdapter;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftTotalsDialogFragment extends PayableItemTotalsDialogFragment<AdditionalShift> {

    public static AdditionalShiftTotalsDialogFragment newInstance(boolean selected) {
        AdditionalShiftTotalsDialogFragment fragment = new AdditionalShiftTotalsDialogFragment();
        fragment.setArguments(getArgs(selected));
        return fragment;
    }

    @NonNull
    @Override
    FilteredItemTotalsAdapter<AdditionalShift> createAdapter(@NonNull Context context) {
        return new AdditionalShiftTotalsAdapter(context, isSelected() ? R.string.selected_additional_shifts : R.string.all_additional_shifts, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShift> getViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

}
