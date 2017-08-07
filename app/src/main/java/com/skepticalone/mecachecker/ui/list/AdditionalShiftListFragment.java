package com.skepticalone.mecachecker.ui.list;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.ui.TotalsDialogFragment;
import com.skepticalone.mecachecker.ui.totals.AdditionalShiftTotalsDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<AdditionalShiftEntity, AdditionalShiftViewModel> {

    @Override
    int getItemType() {
        return R.id.additional;
    }

    @NonNull
    @Override
    ItemListAdapter<AdditionalShiftEntity> createAdapter(Context context) {
        return new AdditionalShiftListAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<AdditionalShiftEntity> createSummaryDialogFragment() {
        return new AdditionalShiftTotalsDialogFragment();
    }

}
