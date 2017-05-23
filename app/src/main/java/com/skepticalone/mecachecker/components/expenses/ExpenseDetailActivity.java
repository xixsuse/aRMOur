package com.skepticalone.mecachecker.components.expenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    static final String EXPENSE_ID = "EXPENSE_ID";
    static final long NO_ID = -1L;
    static final int DETAIL_LOADER_ID = 1;
    private static final String EXPENSE_FRAGMENT = "EXPENSE_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, ExpenseDetailFragment.create(getIntent().getLongExtra(EXPENSE_ID, NO_ID)), EXPENSE_FRAGMENT)
                    .commit();
        }
    }
}
