package com.skepticalone.mecachecker.ui;

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

public final class MainActivity extends CoordinatorActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, ListFragment.Callbacks {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT", DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;

    @Override
    int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemReselectedListener(this);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
        mFabMenu = findViewById(R.id.fab_menu);
        mFabNormalDay = mFabMenu.findViewById(R.id.fab_normal_day);
        mFabLongDay = mFabMenu.findViewById(R.id.fab_long_day);
        mFabNightShift = mFabMenu.findViewById(R.id.fab_night_shift);
        mFabMenu.close(false);
//        if (savedInstanceState == null) {
//            navigation.setSelectedItemId(navigation.getSelectedItemId());
//        } else if (mTwoPane) {
//            getSupportFragmentManager().findFragmentByTag()
//        }
        navigation.setSelectedItemId(navigation.getSelectedItemId());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ListFragment listFragment;
        switch (item.getItemId()) {
//            case R.id.rostered:
//                listFragment = new RosteredShiftListFragment();
//                break;
//            case R.id.additional:
//                listFragment = new AdditionalShiftListFragment();
//                break;
            case R.id.cross_cover:
                listFragment = new CrossCoverListFragment();
                break;
            case R.id.expenses:
                listFragment = new ExpenseListFragment();
                break;
            default:
                return false;
        }
        Bundle args = new Bundle();
        args.putBoolean(ListFragment.IS_TWO_PANE, mTwoPane);
        listFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_fragment_container, listFragment, LIST_FRAGMENT);
        if (mTwoPane) {
            transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(item.getItemId()), DETAIL_FRAGMENT);
        }
        transaction.commit();
        return true;
    }

    @Override
    public final void showSnackbar(@StringRes int text, @StringRes int action, @NonNull View.OnClickListener listener) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).setAction(action, listener).show();
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        if (getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT) == null || (mTwoPane && getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT) == null)) {
            onNavigationItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(int itemType, long itemId) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
            intent.putExtra(DetailActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
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
