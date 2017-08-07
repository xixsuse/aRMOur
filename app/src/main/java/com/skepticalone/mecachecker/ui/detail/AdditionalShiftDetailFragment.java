package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.AdditionalShiftDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.ui.dialog.AdditionalShiftDateDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.AdditionalShiftPaymentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.AdditionalShiftTimeDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

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

}
