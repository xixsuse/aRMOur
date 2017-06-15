package com.skepticalone.mecachecker.components;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.R;


abstract class CoordinatorActivity extends AppCompatActivity implements PickerDialogFragment.Callbacks {

    @Nullable
    private CoordinatorLayout mCoordinatorLayout = null;

    final void setCoordinatorLayout(@Nullable CoordinatorLayout coordinatorLayout) {
        mCoordinatorLayout = coordinatorLayout;
    }

    @Override
    public final void onOverlappingShifts() {
        if (mCoordinatorLayout != null) {
            Snackbar.make(mCoordinatorLayout, R.string.overlapping_shifts, Snackbar.LENGTH_SHORT).show();
        }
    }
}
