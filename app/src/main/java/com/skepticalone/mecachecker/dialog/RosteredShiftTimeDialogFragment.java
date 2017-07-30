package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;

import org.joda.time.LocalTime;

public final class RosteredShiftTimeDialogFragment extends TimeDialogFragment<RosteredShiftEntity, RosteredShiftViewModel> {

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
    RosteredShiftViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @Override
    void onTimeSet(@NonNull LocalTime time, boolean start) {
        getViewModel().saveNewTime(getCurrentItem(), time, start, logged);
    }

}
