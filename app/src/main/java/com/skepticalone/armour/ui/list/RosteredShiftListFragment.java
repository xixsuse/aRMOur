package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RosteredShiftListAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.totals.RosteredShiftTotalsDialogFragment;
import com.skepticalone.armour.ui.totals.RunReviewDialogFragment;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShift> {

    private RosteredShiftListAdapter adapter;
    private RosteredShiftViewModel viewModel;

    @Override
    int getItemType() {
        return R.id.rostered;
    }

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new RosteredShiftListAdapter(context, this);
    }

    @NonNull
    @Override
    protected RosteredShiftListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    protected RosteredShiftViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    RosteredShiftTotalsDialogFragment createSummaryDialogFragment(boolean subtotals) {
        return RosteredShiftTotalsDialogFragment.newInstance(subtotals);
    }

    @Override
    void inflateSelectionMenu(MenuInflater inflater, Menu menu) {
        inflater.inflate(R.menu.logged_shift_menu, menu);
        super.inflateSelectionMenu(inflater, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.logged_shifts) {
            showDialogFragment(new RunReviewDialogFragment());
            return true;
        }
        return super.onActionItemClicked(mode, item);
    }
}
