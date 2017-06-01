package com.skepticalone.mecachecker.components.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SummaryActivity extends AppCompatActivity {

    static final int LOADER_ID_ROSTERED = 1, LOADER_ID_ADDITIONAL = 2, LOADER_ID_CROSS_COVER = 3;
    private static final String SUMMARY_FRAGMENT = "SUMMARY_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new AdditionalSummaryFragment(), SUMMARY_FRAGMENT)
                    .commit();
        }
    }

}
