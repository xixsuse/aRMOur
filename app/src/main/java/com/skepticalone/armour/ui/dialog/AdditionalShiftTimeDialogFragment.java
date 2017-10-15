package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;

import org.threeten.bp.LocalTime;

public final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<AdditionalShift> {

    private AdditionalShiftViewModel viewModel;

    public static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
        AdditionalShiftTimeDialogFragment fragment = new AdditionalShiftTimeDialogFragment();
        fragment.setArguments(getArgs(start));
        return fragment;
    }

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    AdditionalShiftViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    Shift.Data getShiftDataForDisplay(@NonNull AdditionalShift shift) {
        return shift.getShiftData();
    }

    @Override
    void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        getViewModel().saveNewTime(time, isStart);
    }

}
