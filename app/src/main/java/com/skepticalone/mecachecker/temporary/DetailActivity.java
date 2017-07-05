package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;

public class DetailActivity extends LifecycleAppCompatActivity {

    static final String EXPENSE_ID = "EXPENSE_ID";
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    int getContentViewWithToolbar() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ExpenseViewModel model = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        model.selectExpense(getIntent().getLongExtra(EXPENSE_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_fragment_container, new DetailFragment(), DETAIL_FRAGMENT)
                    .commit();
        }
    }
}
