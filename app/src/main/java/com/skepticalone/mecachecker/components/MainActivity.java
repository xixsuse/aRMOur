package com.skepticalone.mecachecker.components;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.settings.SettingsActivity;
import com.skepticalone.mecachecker.summary.SummaryActivity;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        PickerDialogFragment.Callbacks,
        RosteredShiftsListFragment.Listener,
        AdditionalShiftsListFragment.Listener,
        CrossCoverListFragment.Listener,
        ExpensesListFragment.Listener {

    private View mCoordinatorView;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, true);
        setContentView(R.layout.main_activity);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mCoordinatorView = drawer.findViewById(R.id.coordinator);
        Toolbar toolbar = mCoordinatorView.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        FloatingActionButton fab = mCoordinatorView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mTwoPane = findViewById(R.id.detail_container) != null;
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.rostered);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, new RosteredShiftsListFragment(), LifecycleConstants.LIST_FRAGMENT)
                    .commit();
        }
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
            if (id == R.id.rostered) {
                newListFragment = new RosteredShiftsListFragment();
            } else if (id == R.id.additional) {
                newListFragment = new AdditionalShiftsListFragment();
            } else if (id == R.id.cross_cover) {
                newListFragment = new CrossCoverListFragment();
            } else if (id == R.id.expenses) {
                newListFragment = new ExpensesListFragment();
            } else throw new IllegalStateException();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, newListFragment, LifecycleConstants.LIST_FRAGMENT);
            Fragment oldDetailFragment = getSupportFragmentManager().findFragmentByTag(LifecycleConstants.DETAIL_FRAGMENT);
            if (oldDetailFragment != null) transaction.remove(oldDetailFragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDetailFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, fragment, LifecycleConstants.DETAIL_FRAGMENT)
                .commit();
    }

    private void startDetailActivity(int itemType, long id) {
        startActivity(DetailActivity.getIntent(this, itemType, id));
    }

    @Override
    public void onRosteredShiftClicked(long id) {
        if (mTwoPane) {
            showDetailFragment(RosteredShiftDetailFragment.create(id));
        } else {
            startDetailActivity(LifecycleConstants.ITEM_TYPE_ROSTERED_SHIFT, id);
        }
    }

    @Override
    public void onAdditionalShiftClicked(long id) {
        if (mTwoPane) {
            showDetailFragment(AdditionalShiftDetailFragment.create(id));
        } else {
            startDetailActivity(LifecycleConstants.ITEM_TYPE_ADDITIONAL_SHIFT, id);
        }
    }

    @Override
    public void onCrossCoverClicked(long id) {
        if (mTwoPane) {
            showDetailFragment(CrossCoverDetailFragment.create(id));
        } else {
            startDetailActivity(LifecycleConstants.ITEM_TYPE_CROSS_COVER, id);
        }
    }

    @Override
    public void onExpenseClicked(long id) {
        if (mTwoPane) {
            showDetailFragment(ExpenseDetailFragment.create(id));
        } else {
            startDetailActivity(LifecycleConstants.ITEM_TYPE_EXPENSE, id);
        }
    }

    @Override
    public void onOverlappingShifts() {
        Snackbar.make(mCoordinatorView, R.string.overlapping_shifts, Snackbar.LENGTH_SHORT);
    }
}