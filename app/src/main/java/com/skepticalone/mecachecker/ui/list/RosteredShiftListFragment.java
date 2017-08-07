package com.skepticalone.mecachecker.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.RosteredShiftListAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.ui.totals.RosteredShiftTotalsDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShiftEntity> {

    private RosteredShiftListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new RosteredShiftListAdapter(this, ShiftUtil.Calculator.getInstance(context));
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
