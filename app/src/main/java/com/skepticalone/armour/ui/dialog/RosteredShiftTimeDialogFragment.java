package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

import org.threeten.bp.LocalTime;

public final class RosteredShiftTimeDialogFragment extends TimeDialogFragment<RawRosteredShiftEntity> {

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
    RawShift.RawShiftData getShiftDataForDisplay(@NonNull RawRosteredShiftEntity shift) {
        final RawShift.RawShiftData rawShiftData;
        if (logged) {
            rawShiftData = shift.getLoggedShiftData();
            if (rawShiftData == null) throw new IllegalStateException();
        } else {
            rawShiftData = shift.getShiftData();
        }
        return rawShiftData;
    }

    @NonNull
    @Override
    RosteredShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

    @Override
    void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        getViewModel().saveNewTime(time, isStart, logged);
    }

}
