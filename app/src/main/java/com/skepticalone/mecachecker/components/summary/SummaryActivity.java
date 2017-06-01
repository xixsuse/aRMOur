package com.skepticalone.mecachecker.components.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.R;


public class SummaryActivity extends AppCompatActivity {

    static final int LOADER_ID_ROSTERED = 1, LOADER_ID_ADDITIONAL = 2, LOADER_ID_CROSS_COVER = 3;
    private static final String SUMMARY_FRAGMENT = "SUMMARY_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 2;

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AdditionalSummaryFragment();
                case 1:
                    return new AdditionalSummaryFragment();
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(AdditionalSummaryFragment.tabTitle);
                case 1:
                    return getString(AdditionalSummaryFragment.tabTitle);
                default:
                    throw new IllegalStateException();
            }
        }
    }
}
