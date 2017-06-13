package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.skepticalone.mecachecker.R;


public class DetailActivity extends AppCompatActivity {
    static final int
            ITEM_TYPE_ROSTERED_SHIFT = 1,
            ITEM_TYPE_ADDITIONAL_SHIFT = 2,
            ITEM_TYPE_CROSS_COVER = 3,
            ITEM_TYPE_EXPENSE = 4;
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
        setContentView(R.layout.detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
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
                    .add(R.id.item_detail_container, fragment, LifecycleConstants.DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
