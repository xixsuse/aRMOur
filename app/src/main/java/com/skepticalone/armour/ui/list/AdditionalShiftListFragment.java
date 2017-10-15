package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.AdditionalShiftListAdapter;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.ui.totals.AdditionalShiftTotalsDialogFragment;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<AdditionalShift> {

    private AdditionalShiftListAdapter adapter;
    private AdditionalShiftViewModel viewModel;

    @Override
    int getItemType() {
        return R.id.additional;
    }

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new AdditionalShiftListAdapter(context, this);
    }

    @NonNull
    @Override
    protected AdditionalShiftListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    protected AdditionalShiftViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    AdditionalShiftTotalsDialogFragment createSummaryDialogFragment(boolean subtotals) {
        return AdditionalShiftTotalsDialogFragment.newInstance(subtotals);
    }

}
