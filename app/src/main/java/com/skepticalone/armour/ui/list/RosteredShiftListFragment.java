package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.newData.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;
import com.skepticalone.armour.util.ShiftUtil;

import java.util.List;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviders.of(getActivity()).get(com.skepticalone.armour.data.newData.RosteredShiftViewModel.class).getRosteredShifts().observe(this, new Observer<List<RosteredShift>>() {
            private static final String TAG = "Observer";

            @Override
            public void onChanged(@Nullable List<RosteredShift> shifts) {
                if (shifts != null) {
                    Log.d(TAG, "Changed...");
                    for (RosteredShift shift : shifts) {
                        Log.d(TAG, shift.toString());
                    }
                }
            }
        });

    }
}
