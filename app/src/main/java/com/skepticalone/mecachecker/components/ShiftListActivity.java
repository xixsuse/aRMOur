package com.skepticalone.mecachecker.components;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skepticalone.mecachecker.R;

public class ShiftListActivity extends AppCompatActivity implements ShiftListFragment.Listener, ShiftOverlapListener {

    private View mCoordinatorView;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, true);
        setContentView(R.layout.shift_list_activity);
        mCoordinatorView = findViewById(R.id.coordinator_layout);
        mTwoPane = findViewById(R.id.shift_detail_fragment_container) != null;
        ((ViewPager) findViewById(R.id.pager))
                .setAdapter(new ShiftListPagerAdapter(getSupportFragmentManager(), this));
    }

    @Override
    public void onShiftClicked(long shiftId) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.shift_detail_fragment_container, ShiftDetailFragment.create(shiftId))
                    .commit();
        } else {
            Intent intent = new Intent(this, ShiftDetailActivity.class);
            intent.putExtra(ShiftDetailFragment.SHIFT_ID, shiftId);
            startActivity(intent);
        }
    }

    @Override
    public void onShiftOverlap() {
        Snackbar.make(mCoordinatorView, R.string.overlapping_shifts, Snackbar.LENGTH_LONG).show();
    }


}
