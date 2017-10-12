package com.skepticalone.armour.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.CrossCoverListAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.ui.totals.CrossCoverTotalsDialogFragment;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCover> {

    private final CrossCoverListAdapter adapter = new CrossCoverListAdapter(this);

    @Override
    int getItemType() {
        return R.id.cross_cover;
    }

    @Override
    int getQuantityStringResource() {
        return R.plurals.cross_cover_shifts;
    }

    @NonNull
    @Override
    protected CrossCoverListAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    CrossCoverTotalsDialogFragment createSummaryDialogFragment() {
        return new CrossCoverTotalsDialogFragment();
    }
}
