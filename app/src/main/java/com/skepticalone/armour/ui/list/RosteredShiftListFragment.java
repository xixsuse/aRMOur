package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShift> {

    private RosteredShiftListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new RosteredShiftListAdapter(this);
    }

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

    @Override
    int getQuantityStringResource() {
        return R.plurals.rostered_shifts;
    }

    @NonNull
    @Override
    RosteredShiftTotalsDialogFragment createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

}
