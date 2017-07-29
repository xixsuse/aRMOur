package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;

public final class MainActivity extends CoordinatorActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, ListFragment.Callbacks {
    private static final String ITEM_TYPE = "ITEM_TYPE";
    private BottomNavigationView navigation;
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;
    @Nullable
    private LiveData<Long> selectedId;

    @Override
    int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public void showDetail(int itemType, long itemId) {
        if (!mTwoPane) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
            intent.putExtra(DetailActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        mFabMenu = findViewById(R.id.fab_menu);
        mFabNormalDay = mFabMenu.findViewById(R.id.fab_normal_day);
        mFabLongDay = mFabMenu.findViewById(R.id.fab_long_day);
        mFabNightShift = mFabMenu.findViewById(R.id.fab_night_shift);
        mFabMenu.close(false);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        int itemType = savedInstanceState == null ? navigation.getSelectedItemId() : savedInstanceState.getInt(ITEM_TYPE);
        navigation.setSelectedItemId(itemType);
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // do nothing
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ITEM_TYPE, navigation.getSelectedItemId());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemType = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment_container, ListFragment.getNewListFragment(itemType));
        if (mTwoPane) {
            transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType));
        }
        transaction.commit();
        return true;
    }

    @Override
    public final void showSnackbar(@StringRes int text, @StringRes int action, @NonNull View.OnClickListener listener) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).setAction(action, listener).show();
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
