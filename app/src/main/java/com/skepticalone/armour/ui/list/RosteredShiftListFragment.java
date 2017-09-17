package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShiftEntity> {

    private final RosteredShiftListAdapter adapter = new RosteredShiftListAdapter(this);

    @NonNull
    @Override
    protected RosteredShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    protected RosteredShiftListAdapter getAdapter() {
        return adapter;
    }

    @Override
    int getItemType() {
        return R.id.rostered;
    }


    @NonNull
    @Override
    RosteredShiftTotalsDialogFragment createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

}
