package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private static final String ITEM_TYPE = "ITEM_TYPE", ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final long NO_ID = -1L;

    static Intent getIntent(Context context, int itemType, long id) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ITEM_TYPE, itemType);
        intent.putExtra(ITEM_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            long id = getIntent().getLongExtra(ITEM_ID, NO_ID);
            if (id == NO_ID) {
                throw new IllegalStateException();
            }
            Fragment fragment;
            switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
                case LifecycleConstants.ITEM_TYPE_ROSTERED_SHIFT:
                    fragment = RosteredShiftDetailFragment.create(id);
                    break;
                case LifecycleConstants.ITEM_TYPE_ADDITIONAL_SHIFT:
                    fragment = AdditionalShiftDetailFragment.create(id);
                    break;
                case LifecycleConstants.ITEM_TYPE_CROSS_COVER:
                    fragment = CrossCoverDetailFragment.create(id);
                    break;
                case LifecycleConstants.ITEM_TYPE_EXPENSE:
                    fragment = ExpenseDetailFragment.create(id);
                    break;
                default:
                    throw new IllegalStateException();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, LifecycleConstants.DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
