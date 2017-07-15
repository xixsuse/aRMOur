package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.dialog.DatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public final class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, com.skepticalone.mecachecker.data.CrossCover, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks, DatePickerDialogFragment.Callbacks, PaymentDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> createAdapter() {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate(long itemId, @NonNull LocalDate currentDate) {
        showDialogFragment(DatePickerDialogFragment.newInstance(itemId, currentDate));
    }

    @Override
    public void onDateSet(long itemId, @NonNull LocalDate date) {
        getViewModel().setDate(itemId, date);
    }

    @Override
    public void changePayment(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
    }

    @Override
    public void savePayment(long id, @NonNull BigDecimal payment) {
        getViewModel().setPayment(id, payment);
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        getViewModel().setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        getViewModel().setPaid(id, paid);
    }

}
