package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.Model;
import com.skepticalone.mecachecker.dialog.DatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public final class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, CrossCoverEntity>
        implements CrossCoverDetailAdapter.Callbacks, DatePickerDialogFragment.Callbacks, PaymentDialogFragment.Callbacks {


    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private CrossCoverViewModel model;

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> onCreateAdapter(Context context) {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    Model<CrossCoverEntity> onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
        return model;
    }

    @Override
    public void changeDate(long itemId, @NonNull LocalDate currentDate) {
        showDialogFragment(DatePickerDialogFragment.newInstance(itemId, currentDate));
    }

    @Override
    public void saveDate(long itemId, @NonNull LocalDate date) {
        model.setDate(itemId, date);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
    }

    @Override
    public void savePayment(long id, @NonNull BigDecimal payment) {
        model.setPayment(id, payment);
    }

    @Override
    public void onClaimedToggled(long id, boolean claimed) {
        model.setClaimed(id, claimed);
    }

    @Override
    public void onPaidToggled(long id, boolean paid) {
        model.setPaid(id, paid);
    }

    @Override
    public int getRowNumberDate() {
        return ROW_NUMBER_DATE;
    }

    @Override
    public int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    public int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    public int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    public int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    public int getRowCount(@NonNull CrossCover item) {
        return ROW_COUNT;
    }

}
