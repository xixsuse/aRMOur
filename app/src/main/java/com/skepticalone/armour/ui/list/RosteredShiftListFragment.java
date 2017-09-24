package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RawRosteredShiftEntity> {

    private RosteredShiftListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new RosteredShiftListAdapter(this, LiveShiftConfig.getInstance(context));
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


    @NonNull
    @Override
    RosteredShiftTotalsDialogFragment createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

}
