package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.Model;
import com.skepticalone.mecachecker.model.CrossCover;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCover, CrossCoverEntity> {

    private CrossCoverViewModel model;

    @NonNull
    @Override
    ItemListAdapter<CrossCover> createAdapter() {
        return new CrossCoverListAdapter(this);
    }

    @Override
    void onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    Model<CrossCoverEntity> getViewModel() {
        return model;
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_CROSS_COVER;
    }

    @Override
    void addNewItem() {
        model.addNewItem();
    }

}