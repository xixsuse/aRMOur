package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.skepticalone.mecachecker.R;

public class ShiftDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.scroll_view, ShiftDetailFragment.create(getIntent().getLongExtra(ShiftDetailFragment.SHIFT_ID, ShiftDetailFragment.NO_ID)))
                    .commit();
        }
    }

}
