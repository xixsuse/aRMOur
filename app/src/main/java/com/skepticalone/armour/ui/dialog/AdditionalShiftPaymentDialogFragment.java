package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;

public final class AdditionalShiftPaymentDialogFragment extends PaymentDialogFragment<AdditionalShift> {

    private PayableViewModelContract<AdditionalShift> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    PayableViewModelContract<AdditionalShift> getViewModel() {
        return viewModel;
    }

    @Override
    int getTitle() {
        return R.string.hourly_rate;
    }

}
