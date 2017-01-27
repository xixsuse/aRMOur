package com.skepticalone.mecachecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * An activity representing a single Shift detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ShiftListActivity}.
 */
public class ShiftDetailActivity extends AppCompatActivity implements TimePickerFragment.Callback {

    public static final String DETAIL_FRAGMENT_ID = "DETAIL_FRAGMENT_ID";
    private int mStartHours = 9, mStartMinutes = 15, mEndHours = 17, mEndMinutes = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_detail);
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
            Bundle arguments = new Bundle();
            arguments.putLong(ShiftDetailFragment.ARG_START,
                    getIntent().getLongExtra(ShiftDetailFragment.ARG_START));
            ShiftDetailFragment fragment = new ShiftDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.shift_detail_container, fragment, DETAIL_FRAGMENT_ID)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ShiftListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        mStartHours = hourOfDay;
        mStartMinutes = minute;
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_ID);
        if (fragment != null){
            fragment.setTime(true, mStartHours, mStartMinutes);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        mEndHours = hourOfDay;
        mEndMinutes = minute;
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_ID);
        if (fragment != null){
            fragment.setTime(false, mEndHours, mEndMinutes);
        }
    }

    public void editStartTime(View v){
        TimePickerFragment fragment = TimePickerFragment.create(true, mStartHours, mStartMinutes);
        fragment.show(getSupportFragmentManager(), "startTimePicker");
    }

    public void editEndTime(View v){
        TimePickerFragment fragment = TimePickerFragment.create(false, mEndHours, mEndMinutes);
        fragment.show(getSupportFragmentManager(), "endTimePicker");
    }

//    public int getStartHours() {
//        return mStartHours;
//    }
//
//    public int getStartMinutes() {
//        return mStartMinutes;
//    }
//
//    public int getEndHours() {
//        return mEndHours;
//    }
//
//    public int getEndMinutes() {
//        return mEndMinutes;
//    }
}
