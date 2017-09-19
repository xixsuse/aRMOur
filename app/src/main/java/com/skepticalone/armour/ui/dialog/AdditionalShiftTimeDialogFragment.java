package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;

import org.threeten.bp.LocalTime;

public final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<AdditionalShiftEntity> {

    public static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
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
    void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        getViewModel().saveNewTime(time, isStart);
    }

}
