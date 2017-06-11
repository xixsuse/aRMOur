package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {
    static final int
            ITEM_TYPE_ROSTERED_SHIFT = 1,
            ITEM_TYPE_ADDITIONAL_SHIFT = 2,
            ITEM_TYPE_CROSS_COVER = 3,
            ITEM_TYPE_EXPENSE = 4;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final String ITEM_TYPE = "ITEM_TYPE";
    private static final int NO_ITEM_TYPE = 0;

    static Intent getIntent(Context context, int itemType, long id) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ITEM_TYPE, itemType);
        intent.putExtra(DetailFragment.ITEM_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            long id = getIntent().getLongExtra(DetailFragment.ITEM_ID, DetailFragment.NO_ID);
            if (id == DetailFragment.NO_ID) {
                throw new IllegalStateException();
            }
            Fragment fragment;
            switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
                case ITEM_TYPE_ROSTERED_SHIFT:
                    fragment = RosteredShiftDetailFragment.create(id);
                    break;
                case ITEM_TYPE_ADDITIONAL_SHIFT:
                    fragment = AdditionalShiftDetailFragment.create(id);
                    break;
                case ITEM_TYPE_CROSS_COVER:
                    fragment = CrossCoverDetailFragment.create(id);
                    break;
                case ITEM_TYPE_EXPENSE:
                    fragment = ExpenseDetailFragment.create(id);
                    break;
                default:
                    throw new IllegalStateException();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
