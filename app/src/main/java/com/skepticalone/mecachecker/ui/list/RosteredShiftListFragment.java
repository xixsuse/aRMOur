package com.skepticalone.mecachecker.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftListAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.ui.TotalsDialogFragment;
import com.skepticalone.mecachecker.ui.totals.RosteredShiftTotalsDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShiftEntity, RosteredShiftViewModel> {

    @Override
    int getItemType() {
        return R.id.rostered;
    }

    @NonNull
    @Override
    ItemListAdapter<RosteredShiftEntity> createAdapter(Context context) {
        return new RosteredShiftListAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<RosteredShiftEntity> createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

}
