package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.model.CrossCover;

public final class CrossCoverListFragment
        extends SingleAddListFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel> {

    @NonNull
    @Override
    ItemListAdapter<CrossCover> createAdapter() {
        return new CrossCoverListAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_CROSS_COVER;
    }

    @Override
    void addNewItem() {
        getViewModel().addNewItem();
    }

}