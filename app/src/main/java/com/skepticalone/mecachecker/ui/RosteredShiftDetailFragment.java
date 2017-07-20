package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.dialog.RosteredShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.RosteredShiftTimePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.RosteredShiftTimeSetter;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShift, RosteredShiftEntity, RosteredShiftViewModel>
        implements RosteredShiftDetailAdapter.Callbacks, RosteredShiftTimeSetter {

    @NonNull
    @Override
    ItemDetailAdapter<RosteredShift> createAdapter(Context context) {
        return new RosteredShiftDetailAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @Override
    public void changeDate(long id, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData) {
        showDialogFragment(RosteredShiftDatePickerDialogFragment.newInstance(id, shiftData, loggedShiftData));
    }

    @Override
    public void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData) {
        // TODO: 19/07/17
        showDialogFragment(RosteredShiftTimePickerDialogFragment.newInstance(id, isStart, false, shiftData, loggedShiftData));
    }

    @Override
    public void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @Nullable LocalTime loggedStart, @Nullable LocalTime loggedEnd) {
        getViewModel().setShiftTimes(id, date, start, end, loggedStart, loggedEnd);
    }
}
