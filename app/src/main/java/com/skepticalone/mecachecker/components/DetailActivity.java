package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;

import com.skepticalone.mecachecker.R;

public class DetailActivity extends CoordinatorActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Toolbar toolbar = mCoordinatorLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, DetailFragment.create(
                            getIntent().getIntExtra(LifecycleConstants.ITEM_TYPE, LifecycleConstants.ITEM_TYPE_NONE),
                            getIntent().getLongExtra(LifecycleConstants.ITEM_ID, LifecycleConstants.NO_ID)
                    ), LifecycleConstants.DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
