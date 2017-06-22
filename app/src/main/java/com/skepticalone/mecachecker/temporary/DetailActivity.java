package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;

public class DetailActivity extends LifecycleActivity {

    static final String EXPENSE_ID = "EXPENSE_ID";
    private static final long NO_ID = -1L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpenseViewModel model = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        model.selectExpense(getIntent().getLongExtra(EXPENSE_ID, NO_ID));
        setContentView(R.layout.detail_activity);
    }
}
