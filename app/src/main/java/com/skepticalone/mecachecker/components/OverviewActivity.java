//package com.skepticalone.mecachecker.components;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//public class OverviewActivity extends AppCompatActivity implements ShiftListFragment.Listener {
//    private static final String
//            LIST_FRAGMENT = "LIST_FRAGMENT",
//            DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(android.R.id.content, new ShiftListFragment(), LIST_FRAGMENT)
//                    .commit();
//        }
//    }
//
//    @Override
//    public void onShiftClicked(long shiftId) {
//        ShiftDetailFragment fragment = ShiftDetailFragment.create(shiftId);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(android.R.id.content, fragment, DETAIL_FRAGMENT)
//                .addToBackStack(null)
//                .commit();
//    }
//
//}
