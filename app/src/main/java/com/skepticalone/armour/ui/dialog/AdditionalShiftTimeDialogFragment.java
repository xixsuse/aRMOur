package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;

import org.threeten.bp.LocalTime;

public final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<RawAdditionalShiftEntity> {

    public static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
        AdditionalShiftTimeDialogFragment fragment = new AdditionalShiftTimeDialogFragment();
        fragment.setArguments(getArgs(start));
        return fragment;
    }

    @NonNull
    @Override
    RawShift.RawShiftData getShiftDataForDisplay(@NonNull RawAdditionalShiftEntity shift) {
        return shift.getRawShiftData();
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
