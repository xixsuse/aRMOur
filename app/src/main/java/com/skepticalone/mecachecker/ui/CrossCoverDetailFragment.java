package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.ui.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;

public class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> onCreateAdapter() {
        return new CrossCoverDetailAdapter(this);
    }

    @Override
    Class<CrossCoverViewModel> getViewModelClass() {
        return CrossCoverViewModel.class;
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        getViewModel().setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        getViewModel().setPaid(id, paid);
    }

}
