package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skepticalone.mecachecker.R;

public class ShiftDetailActivity extends AppCompatActivity implements ShiftOverlapListener {

    public static final long NO_ID = -1L;
    static final String SHIFT_ID = "SHIFT_ID";
    static final String RAW = "RAW";
    private View mCoordinatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_detail_activity);
        mCoordinatorView = findViewById(R.id.coordinator_layout);
        if (savedInstanceState == null) {
            long shiftId = getIntent().getLongExtra(SHIFT_ID, NO_ID);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator_layout, getIntent().getBooleanExtra(RAW, false) ? LoggedShiftDetailFragment.create(shiftId) : ShiftDetailFragment.create(shiftId))
                    .commit();
        }
    }

    @Override
    public void onShiftOverlap() {
        Snackbar.make(mCoordinatorView, R.string.overlapping_shifts, Snackbar.LENGTH_LONG).show();
    }
}
