package com.skepticalone.mecachecker.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.ui.TotalsDialogFragment;
import com.skepticalone.mecachecker.ui.totals.CrossCoverTotalsDialogFragment;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCoverEntity> {

    private final CrossCoverListAdapter adapter = new CrossCoverListAdapter(this);

    @Override
    int getItemType() {
        return R.id.cross_cover;
    }

    @NonNull
    @Override
    ItemListAdapter<CrossCoverEntity> getAdapter() {
        return adapter;
    }

    @Override
    void addNewItem() {
        getViewModel().addNewCrossCoverShift();
    }

    @NonNull
    @Override
    CrossCoverViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<CrossCoverEntity> createSummaryDialogFragment() {
        return new CrossCoverTotalsDialogFragment();
    }

}
