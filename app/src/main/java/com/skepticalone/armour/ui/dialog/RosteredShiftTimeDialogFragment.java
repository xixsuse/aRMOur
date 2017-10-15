package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

import org.threeten.bp.LocalTime;

public final class RosteredShiftTimeDialogFragment extends TimeDialogFragment<RosteredShift> {

    private static final String LOGGED = "LOGGED";
    private boolean logged;
    private RosteredShiftViewModel viewModel;

    public static RosteredShiftTimeDialogFragment newInstance(boolean start, boolean logged) {
        Bundle arguments = getArgs(start);
        arguments.putBoolean(LOGGED, logged);
        RosteredShiftTimeDialogFragment fragment = new RosteredShiftTimeDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    RosteredShiftViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logged = getArguments().getBoolean(LOGGED);
    }

    @NonNull
    @Override
    Shift.Data getShiftDataForDisplay(@NonNull RosteredShift shift) {
        final Shift.Data shiftData;
        if (logged) {
            shiftData = shift.getLoggedShiftData();
            if (shiftData == null) throw new IllegalStateException();
        } else {
            shiftData = shift.getShiftData();
        }
        return shiftData;
    }

    @Override
    void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        getViewModel().saveNewTime(time, isStart, logged);
    }

}
