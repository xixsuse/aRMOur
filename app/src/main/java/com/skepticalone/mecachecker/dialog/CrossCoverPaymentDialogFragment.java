package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;

public final class CrossCoverPaymentDialogFragment extends PaymentDialogFragment<CrossCoverEntity> {

    @Override
    int getTitle() {
        return R.string.payment;
    }

    @NonNull
    @Override
    PayableViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(CrossCoverViewModel.class);
    }

}
