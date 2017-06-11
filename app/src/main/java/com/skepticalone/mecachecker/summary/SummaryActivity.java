package com.skepticalone.mecachecker.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.R;

public class SummaryActivity extends AppCompatActivity {

    static final int
            LOADER_ID_ROSTERED = 1,
            LOADER_ID_ADDITIONAL = 2,
            LOADER_ID_CROSS_COVER = 3,
            LOADER_ID_EXPENSES = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
    }

}
