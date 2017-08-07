package com.skepticalone.mecachecker.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.ui.totals.AdditionalShiftTotalsDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<AdditionalShiftEntity> {

    private AdditionalShiftListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new AdditionalShiftListAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

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
