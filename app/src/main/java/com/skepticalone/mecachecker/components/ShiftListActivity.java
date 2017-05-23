package com.skepticalone.mecachecker.components;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.components.expenses.ExpensesListFragment;
import com.skepticalone.mecachecker.components.summary.SummaryActivity;

public class ShiftListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final int
            LOADER_ID_ROSTERED_LIST = 1,
            LOADER_ID_ROSTERED_DETAIL = 2,
            LOADER_ID_ADDITIONAL_LIST = 3,
            LOADER_ID_ADDITIONAL_DETAIL = 4,
            LOADER_ID_CROSS_COVER_LIST = 5,
            LOADER_ID_CROSS_COVER_DETAIL = 6,
            LOADER_ID_EXPENSES_LIST = 7,
            LOADER_ID_EXPENSES_DETAIL = 8;

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT";

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, true);
        setContentView(R.layout.shift_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.rostered);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, new RosteredShiftsListFragment(), LIST_FRAGMENT)
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
        } else if (id == R.id.rostered) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new RosteredShiftsListFragment(), LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.additional) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new AdditionalShiftsListFragment(), LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.cross_cover) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new CrossCoverListFragment(), LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.expenses) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new ExpensesListFragment(), LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
