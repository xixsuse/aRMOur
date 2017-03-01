package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skepticalone.mecachecker.R;

class ShiftListPagerAdapter extends FragmentStatePagerAdapter {

    private final static int
            ROSTERED_SHIFTS_POSITION = 0,
            ADDITIONAL_SHIFTS_POSITION = 1;
    private final String ROSTERED_SHIFTS_TITLE, ADDITIONAL_SHIFTS_TITLE;

    ShiftListPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        ROSTERED_SHIFTS_TITLE = context.getString(R.string.rostered);
        ADDITIONAL_SHIFTS_TITLE = context.getString(R.string.additional);

    }

    @Override
    public Fragment getItem(int position) {
        return new ShiftListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case ROSTERED_SHIFTS_POSITION:
                return ROSTERED_SHIFTS_TITLE;
            case ADDITIONAL_SHIFTS_POSITION:
                return ADDITIONAL_SHIFTS_TITLE;
            default:
                throw new IllegalArgumentException();
        }
    }
}
