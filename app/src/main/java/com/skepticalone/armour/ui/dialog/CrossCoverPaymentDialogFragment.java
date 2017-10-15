package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;

public final class CrossCoverPaymentDialogFragment extends PaymentDialogFragment<CrossCover> {

    private PayableViewModelContract<CrossCover> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    PayableViewModelContract<CrossCover> getViewModel() {
        return viewModel;
    }

    @Override
    int getTitle() {
        return R.string.payment;
    }

}
