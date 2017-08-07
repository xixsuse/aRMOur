package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CrossCoverDateDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CrossCoverPaymentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.PaymentDialogFragment;

public final class CrossCoverDetailFragment
        extends PayableDetailFragment<CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks {

    @NonNull
    @Override
    PayableDetailAdapter<CrossCoverEntity> createAdapter(Context context) {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new CrossCoverDateDialogFragment());
    }

    @NonNull
    @Override
    CommentDialogFragment getNewCommentDialogFragment() {
        return new CrossCoverCommentDialogFragment();
    }

    @NonNull
    @Override
    PaymentDialogFragment getNewPaymentDialogFragment() {
        return new CrossCoverPaymentDialogFragment();
    }

}
