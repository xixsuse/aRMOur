package com.skepticalone.mecachecker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;

public class MainActivity extends LifecycleAppCompatActivity implements ListFragment.Callbacks {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT", DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final String MASTER_TO_DETAIL = "MASTER_TO_DETAIL";
    private static final String TAG = "MainActivity";
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        Log.i(TAG, "onCreate: mTwoPane = " + mTwoPane);
        mFabMenu = findViewById(R.id.fab_menu);
        mFabNormalDay = mFabMenu.findViewById(R.id.fab_normal_day);
        mFabLongDay = mFabMenu.findViewById(R.id.fab_long_day);
        mFabNightShift = mFabMenu.findViewById(R.id.fab_night_shift);
        mFabMenu.close(false);
        if (savedInstanceState == null) {
            FragmentTransaction transaction =
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.list_fragment_container, new CrossCoverListFragment(), LIST_FRAGMENT);
            if (mTwoPane) {
                transaction.add(R.id.detail_fragment_container, new CrossCoverDetailFragment(), DETAIL_FRAGMENT);
            }
            transaction.commit();
        }
    }

    @Override
    boolean getDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    int getContentViewWithToolbar() {
        return R.layout.main_activity;
    }

    @Override
    public boolean onItemSelected(int itemType, long itemId) {
//        if (!mTwoPane) {
////            model.selectItem(expenseId);
////            ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT);
////            if (listFragment != null) {
////                listFragment.
////            }
////        } else {
//        }
        if (mTwoPane) return false;
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
        intent.putExtra(DetailActivity.ITEM_ID, itemId);
        startActivity(intent);
        return true;
    }

    @Override
    public FloatingActionMenu getFloatingActionMenu() {
        return mFabMenu;
    }

    @Override
    public FloatingActionButton getFabNormalDay() {
        return mFabNormalDay;
    }

    @Override
    public FloatingActionButton getFabLongDay() {
        return mFabLongDay;
    }

    @Override
    public FloatingActionButton getFabNightShift() {
        return mFabNightShift;
    }
}
