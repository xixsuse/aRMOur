package com.skepticalone.armour.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.AdditionalShiftListAdapter;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.ui.totals.AdditionalShiftTotalsDialogFragment;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<AdditionalShiftEntity> {

    private final AdditionalShiftListAdapter adapter = new AdditionalShiftListAdapter(this);

    @NonNull
    @Override
    protected AdditionalShiftListAdapter getAdapter() {
        return adapter;
    }

    @Override
    int getItemType() {
        return R.id.additional;
    }

    @NonNull
    @Override
    protected AdditionalShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    AdditionalShiftTotalsDialogFragment createSummaryDialogFragment() {
        return new AdditionalShiftTotalsDialogFragment();
    }

}
