package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

public final class DetailActivity extends CoordinatorActivity {

    static final String ITEM_TYPE = "ITEM_TYPE";
    static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    int getContentView() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int itemType = getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE), title;
        setTitle(getName(itemType));
        ViewModelProviders.of(this).get(getViewModelClass(itemType)).selectItem(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
