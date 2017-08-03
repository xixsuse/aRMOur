package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.AdditionalShiftDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalTime;

public final class AdditionalShiftDetailFragment
        extends PayableDetailFragment<AdditionalShiftEntity, AdditionalShiftViewModel>
        implements AdditionalShiftDetailAdapter.Callbacks {

    @NonNull
    @Override
    PayableDetailAdapter<AdditionalShiftEntity> createAdapter(Context context) {
        return new AdditionalShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(AdditionalShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new AdditionalShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start) {
        showDialogFragment(AdditionalShiftTimeDialogFragment.newInstance(start));
    }

    @NonNull
    @Override
    CommentDialogFragment getNewCommentDialogFragment() {
        return new AdditionalShiftCommentDialogFragment();
    }

    @NonNull
    @Override
    PaymentDialogFragment getNewPaymentDialogFragment() {
        return new AdditionalShiftPaymentDialogFragment();
    }

    public static final class AdditionalShiftCommentDialogFragment extends CommentDialogFragment<AdditionalShiftEntity> {

        @NonNull
        @Override
        ViewModelContract<AdditionalShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(AdditionalShiftViewModel.class);
        }

    }

    public static final class AdditionalShiftDateDialogFragment extends ShiftDateDialogFragment<AdditionalShiftEntity> {

        @NonNull
        @Override
        DateViewModelContract<AdditionalShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(AdditionalShiftViewModel.class);
        }

    }

    public static final class AdditionalShiftPaymentDialogFragment extends PaymentDialogFragment<AdditionalShiftEntity> {

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

    public static final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<AdditionalShiftEntity, AdditionalShiftViewModel> {

        static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
            AdditionalShiftTimeDialogFragment fragment = new AdditionalShiftTimeDialogFragment();
            fragment.setArguments(getArgs(start));
            return fragment;
        }

        @NonNull
        @Override
        ShiftData getShiftDataForDisplay(@NonNull AdditionalShiftEntity shift) {
            return shift.getShiftData();
        }

        @NonNull
        @Override
        AdditionalShiftViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(AdditionalShiftViewModel.class);
        }

        @Override
        void onTimeSet(@NonNull LocalTime time, boolean start) {
            getViewModel().saveNewTime(getCurrentItem(), time, start);
        }

    }
}
