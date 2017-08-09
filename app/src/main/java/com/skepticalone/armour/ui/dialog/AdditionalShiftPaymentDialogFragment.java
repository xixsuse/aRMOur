package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;

public final class AdditionalShiftPaymentDialogFragment extends PaymentDialogFragment<AdditionalShiftEntity> {

    @Override
    int getTitle() {
        return R.string.hourly_rate;
    }

    @NonNull
    @Override
    PayableViewModelContract<AdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
