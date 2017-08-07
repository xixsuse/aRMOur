package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CrossCoverCommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CrossCoverDateDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CrossCoverPaymentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.PaymentDialogFragment;

public final class CrossCoverDetailFragment
        extends PayableDetailFragment<CrossCoverEntity>
        implements CrossCoverDetailAdapter.Callbacks {

    private final CrossCoverDetailAdapter adapter = new CrossCoverDetailAdapter(this);

    @NonNull
    @Override
    protected CrossCoverDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new CrossCoverDateDialogFragment());
    }

    @NonNull
    @Override
    PaymentDialogFragment<CrossCoverEntity> createPaymentDialogFragment() {
        return new CrossCoverPaymentDialogFragment();
    }

    @NonNull
    @Override
    CommentDialogFragment<CrossCoverEntity> createCommentDialogFragment() {
        return new CrossCoverCommentDialogFragment();
    }

}
