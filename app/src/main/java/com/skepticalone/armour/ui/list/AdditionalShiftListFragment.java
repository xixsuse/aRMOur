package com.skepticalone.armour.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.AdditionalShiftListAdapter;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.entity.LiveShiftConfig;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.ui.totals.AdditionalShiftTotalsDialogFragment;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<RawAdditionalShiftEntity> {

    private AdditionalShiftListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new AdditionalShiftListAdapter(this, LiveShiftConfig.getInstance(context));
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
