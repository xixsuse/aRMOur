package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;

import org.joda.time.LocalTime;

public final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<AdditionalShiftEntity> {

    static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
        AdditionalShiftTimeDialogFragment fragment = new AdditionalShiftTimeDialogFragment();
        fragment.setArguments(getArgs(start));
        return fragment;
    }

    @NonNull
    @Override
    ShiftData getShiftDataForDisplay(@NonNull AdditionalShiftEntity shift) {
        return shift.getShiftData();
    }

    @NonNull
    @Override
    AdditionalShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

    @Override
    void saveNewTime(@NonNull LocalTime time, boolean start) {
        getViewModel().saveNewTime(time, start);
    }

}
