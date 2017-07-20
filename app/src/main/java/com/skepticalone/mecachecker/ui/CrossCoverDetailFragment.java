package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.model.CrossCover;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.dialog.CrossCoverDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public final class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks, CrossCoverDatePickerDialogFragment.Callbacks, PaymentDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> createAdapter(Context context) {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate(long id, @NonNull LocalDate currentDate) {
        showDialogFragment(CrossCoverDatePickerDialogFragment.newInstance(id, currentDate));
    }

    @Override
    public void saveDate(long id, @NonNull LocalDate date) {
        getViewModel().setDate(id, date);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
    }

    @Override
    public void savePayment(long id, @NonNull BigDecimal payment) {
        getViewModel().getPayableModel().setPayment(id, payment);
    }

    @Override
    public void onClaimedToggled(long id, boolean claimed) {
        getViewModel().getPayableModel().setClaimed(id, claimed);
    }

    @Override
    public void onPaidToggled(long id, boolean paid) {
        getViewModel().getPayableModel().setPaid(id, paid);
    }
}
