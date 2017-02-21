package com.skepticalone.mecachecker.components;

import android.app.Activity;
import android.os.Bundle;

public class ShiftDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, ShiftDetailFragment.create(getIntent().getLongExtra(ShiftDetailFragment.SHIFT_ID, ShiftDetailFragment.NO_ID)))
                    .commit();
        }
    }

}
