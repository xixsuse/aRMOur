package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableItemViewModelContract;

public final class AdditionalShiftPaymentDialogFragment extends PaymentDialogFragment<AdditionalShiftEntity> {

    @Override
    int getTitle() {
        return R.string.hourly_rate;
    }

    @NonNull
    @Override
    PayableItemViewModelContract<AdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
