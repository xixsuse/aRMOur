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

public class ShiftListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String SHIFT_LIST_FRAGMENT = "SHIFT_LIST_FRAGMENT";

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
            navigationView.setCheckedItem(R.id.compliance);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, new RosteredShiftsListFragment(), SHIFT_LIST_FRAGMENT)
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
        if (id == R.id.compliance) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new RosteredShiftsListFragment(), SHIFT_LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.additional) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, new AdditionalShiftsListFragment(), SHIFT_LIST_FRAGMENT)
                    .commit();
        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
