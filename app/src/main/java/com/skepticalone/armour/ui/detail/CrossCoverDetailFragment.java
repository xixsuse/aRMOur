package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.CrossCoverDetailAdapter;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverDateDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverPaymentDialogFragment;
import com.skepticalone.armour.ui.dialog.PaymentDialogFragment;

public final class CrossCoverDetailFragment
        extends PayableDetailFragment<RawCrossCoverEntity>
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
    PaymentDialogFragment<RawCrossCoverEntity> createPaymentDialogFragment() {
        return new CrossCoverPaymentDialogFragment();
    }

    @NonNull
    @Override
    CommentDialogFragment<RawCrossCoverEntity> createCommentDialogFragment() {
        return new CrossCoverCommentDialogFragment();
    }

}
