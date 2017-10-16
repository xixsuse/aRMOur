package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.LoggedShiftTotalsAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

import java.util.List;

public final class LoggedShiftTotalsDialogFragment extends ItemTotalsDialogFragment<RosteredShift> {

    private LoggedShiftTotalsAdapter adapter;

    @Override
    int getLayout() {
        return R.layout.logged_shift_totals;
    }

    @Override
    int getTitle() {
        return R.string.logged_shift_totals;
    }

    @Override
    void onCreateAdapter(@NonNull Context context) {
        adapter = new LoggedShiftTotalsAdapter(context);
    }

    @NonNull
    @Override
    LoggedShiftTotalsAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    LiveData<List<RosteredShift>> getObservedItems(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(RosteredShiftViewModel.class).getSelectedItems();
    }

}
