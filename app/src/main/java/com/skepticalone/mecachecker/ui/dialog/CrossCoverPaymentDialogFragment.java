package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
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
    PayableViewModelContract<CrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
