package com.skepticalone.mecachecker.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.AdditionalShiftTotalsAdapter;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftTotalsDialogFragment extends PayableTotalsDialogFragment<AdditionalShiftEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<AdditionalShiftEntity> createAdapter(@NonNull Context context) {
        return new AdditionalShiftTotalsAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    ItemViewModelContract<AdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
