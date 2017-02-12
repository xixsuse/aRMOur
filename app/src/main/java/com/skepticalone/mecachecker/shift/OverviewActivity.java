package com.skepticalone.mecachecker.shift;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;

import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class OverviewActivity extends Activity implements
        ShiftListFragment.Listener {

    //    private static final String[] COLUMNS = {
//            BaseColumns._ID,
//            ShiftProvider.START_TIME,
//            ShiftProvider.END_TIME
//    };
    static final int LOADER_LIST_ID = 0;
    static final int LOADER_DETAIL_ID = 1;
    //    public static final int
//            COLUMN_INDEX_ID = 0,
//            COLUMN_INDEX_START = 1,
//            COLUMN_INDEX_END = 2;
    private static final String TAG = "OverviewActivity";
    private static final String
            LIST_FRAGMENT = "LIST_FRAGMENT",
            DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final int
            DEFAULT_START_HOUR_OF_DAY = 8,
            DEFAULT_START_MINUTE = 0,
            DEFAULT_END_HOUR_OF_DAY = 16,
            DEFAULT_END_MINUTE = 0;
//    private final List<PeriodWithStableId> mShifts = new ArrayList<>();
//    private final LongSparseArray<PeriodWithStableId> mShiftsIndex = new LongSparseArray<>();

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
    public void onAddShiftClicked(Date lastShiftEnd) {
        Calendar start = new GregorianCalendar();
        start.setTime(lastShiftEnd);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MINUTE, DEFAULT_START_MINUTE);
        start.set(Calendar.HOUR_OF_DAY, DEFAULT_START_HOUR_OF_DAY);
        if (lastShiftEnd != null) {
            while (lastShiftEnd.after(start.getTime())) {
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        Calendar end = new GregorianCalendar(
                start.get(Calendar.YEAR),
                start.get(Calendar.MONTH),
                start.get(Calendar.DAY_OF_MONTH),
                DEFAULT_END_HOUR_OF_DAY,
                DEFAULT_END_MINUTE
        );
        while (!(end.after(start))) {
            end.add(Calendar.DAY_OF_MONTH, 1);
        }
        ContentValues values = new ContentValues();
        values.put(ShiftProvider.START_TIME, start.getTimeInMillis() / 1000);
        values.put(ShiftProvider.END_TIME, end.getTimeInMillis() / 1000);
        getContentResolver().insert(ShiftProvider.shiftsUri, values);
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

    @Override
    public void onShiftSwiped(long shiftId) {
        getContentResolver().delete(ShiftProvider.shiftUri(shiftId), null, null);
    }

}
