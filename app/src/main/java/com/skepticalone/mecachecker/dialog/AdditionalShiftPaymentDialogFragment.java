package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;

public final class AdditionalShiftPaymentDialogFragment extends PaymentDialogFragment<AdditionalShiftEntity> {

    @Override
    int getTitle() {
        return R.string.hourly_rate;
    }

    @NonNull
    @Override
    PayableViewModelContract<AdditionalShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

}
