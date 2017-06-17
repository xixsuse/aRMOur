package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.settings.SettingsActivity;
import com.skepticalone.mecachecker.summary.SummaryActivity;

public class MainActivity extends CoordinatorActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ListFragment.Callbacks,
        ShiftTypeAwareItemListFragment.Callbacks,
        SinglePaymentItemListFragment.Callbacks {

    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, true);
        setContentView(R.layout.main_activity);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mCoordinatorLayout = drawer.findViewById(R.id.coordinator);
        mFabMenu = mCoordinatorLayout.findViewById(R.id.fab_menu);
        mFabNormalDay = mCoordinatorLayout.findViewById(R.id.fab_normal_day);
        mFabLongDay = mCoordinatorLayout.findViewById(R.id.fab_long_day);
        mFabNightShift = mCoordinatorLayout.findViewById(R.id.fab_night_shift);
        Toolbar toolbar = mCoordinatorLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.rostered);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, new RosteredShiftsListFragment(), LifecycleConstants.LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void setFab(@NonNull View.OnClickListener fabListener) {
        mFabMenu.setOnMenuButtonClickListener(fabListener);
        mFabMenu.close(true);
    }

    @Override
    public void setFabMenu(@NonNull View.OnClickListener normalDayListener, @NonNull View.OnClickListener longDayListener, @NonNull View.OnClickListener nightShiftListener) {
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.toggle(true);
            }
        });
        mFabNormalDay.setOnClickListener(normalDayListener);
        mFabLongDay.setOnClickListener(longDayListener);
        mFabNightShift.setOnClickListener(nightShiftListener);
        mFabMenu.close(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mFabMenu.isOpened()) {
            mFabMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.summary) {
            startActivity(new Intent(this, SummaryActivity.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else {
            Fragment newListFragment;
            switch (id) {
                case R.id.rostered:
                    newListFragment = new RosteredShiftsListFragment();
                    break;
                case R.id.additional:
                    newListFragment = new AdditionalShiftsListFragment();
                    break;
                case R.id.cross_cover:
                    newListFragment = new CrossCoverListFragment();
                    break;
                case R.id.expenses:
                    newListFragment = new ExpensesListFragment();
                    break;
                default:
                    throw new IllegalStateException();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, newListFragment, LifecycleConstants.LIST_FRAGMENT);
            Fragment oldDetailFragment = getSupportFragmentManager().findFragmentByTag(LifecycleConstants.DETAIL_FRAGMENT);
            if (oldDetailFragment != null) {
                transaction.remove(oldDetailFragment);
            }
            transaction.commit();
        }
        mFabMenu.close(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void launch(int itemType, long itemId) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, DetailFragment.create(itemType, itemId), LifecycleConstants.DETAIL_FRAGMENT)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(LifecycleConstants.ITEM_TYPE, itemType);
            intent.putExtra(LifecycleConstants.ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    @Override
    public void makeDeletedSnack(@NonNull final Uri dirUri, @NonNull final ContentValues values) {
        if (mCoordinatorLayout == null) return;
        Snackbar
                .make(mCoordinatorLayout, R.string.item_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getContentResolver().insert(dirUri, values);
                    }
                })
                .show();
    }

}