package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

import org.joda.time.LocalDate;

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

    public static final class CrossCoverCommentDialogFragment extends CommentDialogFragment<CrossCoverEntity> {

        @NonNull
        @Override
        ViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(CrossCoverViewModel.class);
        }

    }

    public static final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCoverEntity> {

        @NonNull
        @Override
        LocalDate getDateForDisplay(@NonNull CrossCoverEntity crossCover) {
            return crossCover.getDate();
        }

        @NonNull
        @Override
        DateViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(CrossCoverViewModel.class);
        }

    }

    public static final class CrossCoverPaymentDialogFragment extends PaymentDialogFragment<CrossCoverEntity> {

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
}
