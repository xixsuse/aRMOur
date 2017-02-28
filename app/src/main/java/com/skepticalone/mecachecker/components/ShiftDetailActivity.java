package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skepticalone.mecachecker.R;

public class ShiftDetailActivity extends AppCompatActivity implements ShiftOverlapListener {

    private View mCoordinatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_detail_activity);
        mCoordinatorView = findViewById(R.id.coordinator_layout);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator_layout, ShiftDetailFragment.create(getIntent().getLongExtra(ShiftDetailFragment.SHIFT_ID, ShiftDetailFragment.NO_ID)))
                    .commit();
        }
    }

    @Override
    public void onShiftOverlap() {
        Snackbar.make(mCoordinatorView, R.string.overlapping_shifts, Snackbar.LENGTH_LONG).show();
    }
}
