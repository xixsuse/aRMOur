package com.skepticalone.armour.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.ui.common.CoordinatorActivity;

public final class DetailActivity extends CoordinatorActivity {

    public static final String ITEM_TYPE = "ITEM_TYPE";
    public static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    protected int getContentView() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int itemType = getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE);
        setTitle(getName(itemType));
        getViewModel(itemType).setCurrentItemId(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
