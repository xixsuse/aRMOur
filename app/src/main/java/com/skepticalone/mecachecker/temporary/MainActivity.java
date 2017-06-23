package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.ui.ExpenseClickCallback;

public class MainActivity extends LifecycleActivity implements ExpenseClickCallback {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT", DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final String MASTER_TO_DETAIL = "MASTER_TO_DETAIL";
    private static final String TAG = "MainActivity";
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;
    private ExpenseViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        setContentView(R.layout.main_activity);
        mTwoPane = findViewById(R.id.detail_fragment) != null;
        Log.i(TAG, "onCreate: mTwoPane = " + mTwoPane);
        mFabMenu = findViewById(R.id.fab_menu);
        mFabNormalDay = mFabMenu.findViewById(R.id.fab_normal_day);
        mFabLongDay = mFabMenu.findViewById(R.id.fab_long_day);
        mFabNightShift = mFabMenu.findViewById(R.id.fab_night_shift);
        mFabMenu.close(false);
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.addExpense();
            }
        });
    }

    @Override
    public void onClick(long expenseId) {
        if (mTwoPane) {
            model.selectExpense(expenseId);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXPENSE_ID, expenseId);
            startActivity(intent);
        }
    }

}
