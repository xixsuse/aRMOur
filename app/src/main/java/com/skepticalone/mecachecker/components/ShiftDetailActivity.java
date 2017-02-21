package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShiftDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, ShiftDetailFragment.create(getIntent().getLongExtra(ShiftDetailFragment.SHIFT_ID, ShiftDetailFragment.NO_ID)))
                    .commit();
        }
    }

}
