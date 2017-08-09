package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.util.ShiftData;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

import org.joda.time.LocalTime;

public final class RosteredShiftTimeDialogFragment extends TimeDialogFragment<RosteredShiftEntity> {

    private static final String LOGGED = "LOGGED";
    private boolean logged;

    public static RosteredShiftTimeDialogFragment newInstance(boolean start, boolean logged) {
        Bundle arguments = getArgs(start);
        arguments.putBoolean(LOGGED, logged);
        RosteredShiftTimeDialogFragment fragment = new RosteredShiftTimeDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logged = getArguments().getBoolean(LOGGED);
    }

    @NonNull
    @Override
    ShiftData getShiftDataForDisplay(@NonNull RosteredShiftEntity shift) {
        final ShiftData shiftData;
        if (logged) {
            shiftData = shift.getLoggedShiftData();
            if (shiftData == null) throw new IllegalStateException();
        } else {
            shiftData = shift.getShiftData();
        }
        return shiftData;
    }

    @NonNull
    @Override
    RosteredShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

    @Override
    void saveNewTime(@NonNull LocalTime time, boolean start) {
        getViewModel().saveNewTime(time, start, logged);
    }

}
