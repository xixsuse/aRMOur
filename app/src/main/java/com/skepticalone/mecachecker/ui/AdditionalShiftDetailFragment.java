package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.AdditionalShiftDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.dialog.AdditionalShiftTimeSetter;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftTimePickerDialogFragment;
import com.skepticalone.mecachecker.model.AdditionalShift;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;

public final class AdditionalShiftDetailFragment
        extends DetailFragment<AdditionalShift, AdditionalShiftEntity>
        implements AdditionalShiftDetailAdapter.Callbacks, AdditionalShiftTimeSetter, PaymentDialogFragment.Callbacks {

    private AdditionalShiftViewModel model;

    @NonNull
    @Override
    ItemDetailAdapter<AdditionalShift> onCreateAdapter(Context context) {
        return new AdditionalShiftDetailAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    Model<AdditionalShiftEntity> onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
        return model;
    }

    @Override
    public void changeDate(long id, @NonNull ShiftData shiftData) {
        showDialogFragment(ShiftDatePickerDialogFragment.newInstance(id, shiftData));
    }

    @Override
    public void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData) {
        showDialogFragment(ShiftTimePickerDialogFragment.newInstance(id, isStart, shiftData));
    }

    @Override
    public void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        model.setShiftTimes(id, date, start, end);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.hourly_rate));
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
    public int getRowNumberStart() {
        return ROW_NUMBER_START;
    }

    @Override
    public int getRowNumberEnd() {
        return ROW_NUMBER_END;
    }

    @Override
    public int getRowNumberShiftType() {
        return ROW_NUMBER_SHIFT_TYPE;
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
    public int getRowCount(@NonNull AdditionalShift item) {
        return ROW_COUNT;
    }

}
