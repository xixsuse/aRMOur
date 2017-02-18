package com.skepticalone.mecachecker.components;

import android.app.Activity;
import android.os.Bundle;

public class OverviewActivity extends Activity implements ShiftListFragment.Listener {
    static final int
            LOADER_LIST_ID = 0,
            LOADER_DETAIL_ID = 1;
    private static final String
            LIST_FRAGMENT = "LIST_FRAGMENT",
            DETAIL_FRAGMENT = "DETAIL_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new ShiftListFragment(), LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onShiftClicked(long shiftId) {
        ShiftDetailFragment fragment = ShiftDetailFragment.create(shiftId);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment, DETAIL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

}
