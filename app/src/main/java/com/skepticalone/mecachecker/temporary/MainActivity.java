package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.ui.ExpenseClickCallback;

public class MainActivity extends LifecycleAppCompatActivity implements ExpenseClickCallback {

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
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
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
        if (savedInstanceState == null) {
            FragmentTransaction transaction =
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.list_fragment_container, new ListFragment(), LIST_FRAGMENT);
            if (mTwoPane) {
                transaction.add(R.id.detail_fragment_container, new DetailFragment(), DETAIL_FRAGMENT);
            }
            transaction.commit();
        }
    }

    @Override
    int getContentViewWithToolbar() {
        return R.layout.main_activity;
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
