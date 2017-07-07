package com.skepticalone.mecachecker.ui;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.db.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.ui.adapter.CrossCoverListAdapter;

public class CrossCoverListFragment extends ListFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel, CrossCoverListAdapter> {

    private final CrossCoverListAdapter mAdapter = new CrossCoverListAdapter(this);

    @Override
    CrossCoverListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    void setupFab(FloatingActionMenu menu, FloatingActionButton fabNormalDay, FloatingActionButton fabLongDay, FloatingActionButton fabNightShift) {
        menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addRandomCrossCoverShift();
            }
        });

    }

    @Override
    Class<CrossCoverViewModel> getViewModelClass() {
        return CrossCoverViewModel.class;
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_CROSS_COVER;
    }
}
