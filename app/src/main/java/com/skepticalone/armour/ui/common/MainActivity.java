package com.skepticalone.armour.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.skepticalone.armour.R;
import com.skepticalone.armour.help.HelpActivity;
import com.skepticalone.armour.settings.SettingsActivity;
import com.skepticalone.armour.ui.detail.DetailActivity;
import com.skepticalone.armour.ui.detail.DetailFragment;
import com.skepticalone.armour.ui.list.DeletedItemsInfo;
import com.skepticalone.armour.ui.list.ListFragment;

public final class MainActivity extends CoordinatorActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, ListFragment.Callbacks {

    public static final String ITEM_TYPE = "ITEM_TYPE", ITEM_ID = "ITEM_ID";
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
            intent.putExtra(ITEM_TYPE, itemType);
            intent.putExtra(ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        mFabPrimary = findViewById(R.id.fab_primary);
        mFabLongDay = findViewById(R.id.fab_long_day);
        mFabNightShift = findViewById(R.id.fab_night_shift);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemReselectedListener(this);
        if (savedInstanceState == null) {
            setupFragments(navigation.getSelectedItemId(), true);
        } else {
            navigation.setSelectedItemId(savedInstanceState.getInt(ITEM_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ITEM_TYPE, navigation.getSelectedItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
//            case R.id.feedback:
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                return true;
            case R.id.help:
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra(ITEM_TYPE, navigation.getSelectedItemId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupFragments(int itemType, boolean fresh) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment_container, ListFragment.getNewListFragment(itemType));
        if (mTwoPane) {
            transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType));
        } else if (!fresh) {
            Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            if (detailFragment != null) {
                transaction.remove(detailFragment);
            }
        }
        transaction.commit();
    }

    @Override
    public final boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setupFragments(item.getItemId(), false);
        return true;
    }

    @Override
    public final void onNavigationItemReselected(@NonNull MenuItem item) {
        // do nothing
    }

    @Override
    public final void onItemRemoved(@NonNull DeletedItemsInfo deletedItemsInfo) {
        Snackbar.make(mCoordinatorLayout, deletedItemsInfo.getMessage(), Snackbar.LENGTH_LONG).setAction(R.string.undo, deletedItemsInfo).show();
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

    @Override
    public void setNavigationBarVisibility(int visibility) {
        navigation.setVisibility(visibility);
    }

}
