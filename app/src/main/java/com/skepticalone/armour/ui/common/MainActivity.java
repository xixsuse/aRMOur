package com.skepticalone.armour.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.settings.SettingsActivity;
import com.skepticalone.armour.ui.detail.DetailActivity;
import com.skepticalone.armour.ui.detail.DetailFragment;
import com.skepticalone.armour.ui.list.ListFragment;

public final class MainActivity extends CoordinatorActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, ListFragment.Callbacks {
    private static final String ITEM_TYPE = "ITEM_TYPE";
    private BottomNavigationView navigation;
    private FloatingActionButton mFabPrimary, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;

    @Override
    protected int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public void onClick(@IdRes int itemType, long itemId) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
            intent.putExtra(DetailActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // TODO: 22/09/17
//        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, false);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        mFabPrimary = findViewById(R.id.fab_primary);
        mFabLongDay = findViewById(R.id.fab_long_day);
        mFabNightShift = findViewById(R.id.fab_night_shift);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.feedback:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public final void onItemRemoved(@IdRes int itemType, @NonNull View.OnClickListener listener) {
        Snackbar.make(mCoordinatorLayout, getString(R.string.item_removed, getString(getName(itemType))), Snackbar.LENGTH_LONG).setAction(R.string.undo, listener).show();
    }

    @Override
    public FloatingActionButton getFabPrimary() {
        return mFabPrimary;
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
