package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.RosteredShiftTimeSetter;
import com.skepticalone.mecachecker.dialog.ShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftTimePickerDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;

public final class RosteredShiftDetailFragment
        extends DetailFragment<AdditionalShift, RosteredShiftEntity, RosteredShiftViewModel>
        implements RosteredShiftDetailAdapter.Callbacks, RosteredShiftTimeSetter, PaymentDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<AdditionalShift> createAdapter(Context context) {
        return new RosteredShiftDetailAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
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
        getViewModel().setShiftTimes(id, date, start, end);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.hourly_rate));
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
