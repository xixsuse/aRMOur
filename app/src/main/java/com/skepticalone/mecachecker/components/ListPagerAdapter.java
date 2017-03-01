package com.skepticalone.mecachecker.components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class ListPagerAdapter extends FragmentStatePagerAdapter {

    ListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ShiftListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
