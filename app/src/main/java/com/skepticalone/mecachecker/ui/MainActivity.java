package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

//    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

//    private static final String LIST_FRAGMENT = "LIST_FRAGMENT", DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private BottomNavigationView navigation;
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;
    @Nullable
    private LiveData<Long> selectedId;
//
//    @Override
//    public LifecycleRegistry getLifecycle() {
//        return lifecycleRegistry;
//    }

    @Override
    int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public void onItemSelected(int itemType, long itemId) {
        if (!mTwoPane) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
            intent.putExtra(DetailActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    private static String getItemType(@IdRes int itemType) {
        if (itemType == R.id.cross_cover) return "Cross cover";
        if (itemType == R.id.expenses) return "Expenses";
        return "Other: itemType = " + itemType;
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
//        navigation.setSelectedItemId(itemType);
//
//        if (savedInstanceState == null) {
//            navigation.setSelectedItemId(navigation.getSelectedItemId());
//        } else if (mTwoPane) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(navigation.getSelectedItemId()), DETAIL_FRAGMENT)
//                    .commitAllowingStateLoss();
//        }
//        setViewModel(navigation.getSelectedItemId());
//        if (savedInstanceState == null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.list_fragment_container, ListFragment.getNewListFragment(itemType), LIST_FRAGMENT);
//            if (selectedId == null) {
//                transaction.remove(getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT));
//            } else {
//                transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT);
//            }
//            transaction.commit();
//
//        }

//        navigation.setOnNavigationItemReselectedListener(this);
//        if (!mTwoPane) {
//            getSupportFragmentManager().beginTransaction()
//                    .remove(getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT))
//                    .commit();
//        }
//        if (savedInstanceState == null) {
//            navigation.setSelectedItemId(navigation.getSelectedItemId());
//        } else if (mTwoPane) {
//            getSupportFragmentManager().findFragmentByTag()
//        }
//        navigation.setSelectedItemId(navigation.getSelectedItemId());
    }
//
//    @Override
//    public void onChanged(@Nullable Long selectedId) {
//        final String TAG = "onSelectedIdChanged";
//        Log.i(TAG, "onChanged: selectedId=" + selectedId + ", twoPane=" + mTwoPane);
//        if (selectedId != null) {
//            if (mTwoPane) {
//                getSupportFragmentManager().beginTransaction();
//                if (selectedId == null) {
//                    transaction.remove(getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT));
//                } else {
//                    transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT);
//                }
//                transaction.commit();
//            } else {
//                if (selectedId != null) {
//                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                    intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
//                    intent.putExtra(DetailActivity.ITEM_ID, selectedId);
//                    startActivity(intent);
//                }
//            }
//        }
//    }

//    private void setViewModel(@IdRes final int itemType) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.list_fragment_container, ListFragment.getNewListFragment(itemType), LIST_FRAGMENT)
//                .remove(getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT))
//                .commit();
//        if (selectedId != null) selectedId.removeObserver(this);
//        selectedId = ViewModelProviders.of(this).get(getViewModelClass(itemType)).getSelectedId();
//        selectedId.observeForever(this);
//    }
//
//    @Override
//    public void onItemSelected(int itemType, long itemId) {
//
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ITEM_TYPE, navigation.getSelectedItemId());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int itemType = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment_container, ListFragment.getNewListFragment(itemType));
        if (mTwoPane) {
            transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType));
        }
        transaction.commit();
//
//        final String TAG = "onSelectedIdChanged";
//        Log.i(TAG, "onChanged: selectedId=" + selectedId + ", twoPane=" + mTwoPane);
//        if (selectedId != null) {
//            if (mTwoPane) {
//                getSupportFragmentManager().beginTransaction();
//                if (selectedId == null) {
//                    transaction.remove(getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT));
//                } else {
//                    transaction.replace(R.id.detail_fragment_container, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT);
//                }
//                transaction.commit();
//            } else {
//                if (selectedId != null) {
//                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                    intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
//                    intent.putExtra(DetailActivity.ITEM_ID, selectedId);
//                    startActivity(intent);
//                }
//            }
//        }
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
