package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShift> {

    private RosteredShiftListAdapter adapter;
    private RosteredShiftViewModel viewModel;

    @Override
    int getItemType() {
        return R.id.rostered;
    }

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new RosteredShiftListAdapter(context, this);
    }

    @NonNull
    @Override
    protected RosteredShiftListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    protected RosteredShiftViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    RosteredShiftTotalsDialogFragment createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

}
