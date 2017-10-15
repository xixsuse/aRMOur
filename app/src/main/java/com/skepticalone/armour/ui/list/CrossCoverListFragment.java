package com.skepticalone.armour.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.CrossCoverListAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.ui.totals.CrossCoverTotalsDialogFragment;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCover> {

    private CrossCoverListAdapter adapter;
    private CrossCoverViewModel viewModel;

    @Override
    int getItemType() {
        return R.id.cross_cover;
    }

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new CrossCoverListAdapter(context, this);
    }

    @NonNull
    @Override
    protected CrossCoverListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    CrossCoverTotalsDialogFragment createSummaryDialogFragment(boolean subtotals) {
        return CrossCoverTotalsDialogFragment.newInstance(subtotals);
    }

}
