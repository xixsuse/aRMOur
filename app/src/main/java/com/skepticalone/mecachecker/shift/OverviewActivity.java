package com.skepticalone.mecachecker.shift;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LongSparseArray;

import com.skepticalone.mecachecker.Compliance;
import com.skepticalone.mecachecker.PeriodWithStableId;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class OverviewActivity extends Activity implements
        ShiftListFragment.Listener,
        ShiftDetailFragment.Listener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2;
    public static final String TAG = "OverviewActivity";
    private static final String[] COLUMNS = {
            BaseColumns._ID,
            ShiftProvider.START_TIME,
            ShiftProvider.END_TIME
    };
    private static final int LOADER_ID = 0;
    private static final String
            LIST_FRAGMENT = "LIST_FRAGMENT",
            DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final int
            DEFAULT_START_HOUR_OF_DAY = 8,
            DEFAULT_START_MINUTE = 0,
            DEFAULT_END_HOUR_OF_DAY = 16,
            DEFAULT_END_MINUTE = 0;
    private final List<PeriodWithStableId> mShifts = new ArrayList<>();
    private final LongSparseArray<PeriodWithStableId> mShiftsIndex = new LongSparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new ShiftListFragment(), LIST_FRAGMENT)
                    .commit();
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ShiftProvider.shiftsUri, COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, @Nullable Cursor data) {
        mShifts.clear();
        mShiftsIndex.clear();
        if (data != null && data.moveToFirst()) {
            do {
                PeriodWithStableId shift = new PeriodWithStableId(
                        data.getLong(COLUMN_INDEX_START),
                        data.getLong(COLUMN_INDEX_END),
                        data.getLong(COLUMN_INDEX_ID)
                );
                mShifts.add(shift);
                mShiftsIndex.append(shift.getId(), shift);
            } while (data.moveToNext());
            Compliance.checkMinimumRestHoursBetweenShifts(mShifts);
            Compliance.checkMaximumHoursPerDay(mShifts);
            Compliance.checkMaximumHoursPerWeek(mShifts);
            Compliance.checkMaximumHoursPerFortnight(mShifts);
            Compliance.checkMaximumConsecutiveWeekends(mShifts);
//            for (PeriodWithStableId shift : mShifts) {
//                Log.i(TAG, shift.toString());
//            }
        }
        ShiftListFragment listFragment = (ShiftListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT);
        if (listFragment != null) {
            Log.i(TAG, "onLoadFinished: notifying dataset changed");
            listFragment.notifyDataSetChanged();
        } else {
            Log.i(TAG, "onLoadFinished: couldn't find listFragment");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onAddShiftClicked() {
        Calendar start = GregorianCalendar.getInstance();
        Date lastShiftEnd = mShifts.isEmpty() ? null : mShifts.get(mShifts.size() - 1).getEnd();
        if (lastShiftEnd != null) {
            start.setTime(lastShiftEnd);
        }
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
    public void onShiftClicked(PeriodWithStableId shift) {
        ShiftDetailFragment fragment = ShiftDetailFragment.create(shift.getId());
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment, DETAIL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onShiftLongClicked(PeriodWithStableId shift) {
        // TODO: 7/02/17
    }

    @Override
    public void onShiftSwiped(long shiftId) {
        getContentResolver().delete(ShiftProvider.shiftUri(shiftId), null, null);
    }

    @Override
    public int getShiftCount() {
        return mShifts.size();
    }

    @Override
    public PeriodWithStableId getShift(int position) {
        return mShifts.get(position);
    }

    @Override
    public PeriodWithStableId getShift(long id) {
        return mShiftsIndex.get(id);
    }
}
