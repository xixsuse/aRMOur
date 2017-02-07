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

import com.skepticalone.mecachecker.Compliance;
import com.skepticalone.mecachecker.PeriodWithStableId;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class OverviewActivity extends Activity implements
        ShiftListFragment.Listener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2;
    private static final String[] COLUMNS = {
            BaseColumns._ID,
            ShiftProvider.START_TIME,
            ShiftProvider.END_TIME
    };
    private static final int LOADER_ID = 0;
    private static final int
            DEFAULT_START_HOUR_OF_DAY = 8,
            DEFAULT_START_MINUTE = 0,
            DEFAULT_END_HOUR_OF_DAY = 16,
            DEFAULT_END_MINUTE = 0;

    private final List<PeriodWithStableId> mShifts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_list_activity);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ShiftProvider.shiftsUri, COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, @Nullable Cursor data) {
        mShifts.clear();
        if (data != null && data.moveToFirst()) {
            do {
                mShifts.add(new PeriodWithStableId(
                        data.getLong(ShiftListActivity.COLUMN_INDEX_START),
                        data.getLong(ShiftListActivity.COLUMN_INDEX_END),
                        data.getLong(ShiftListActivity.COLUMN_INDEX_ID)
                ));
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
        ShiftListFragment listFragment = (ShiftListFragment) getFragmentManager().findFragmentById(R.id.shift_list_fragment);
        if (listFragment != null) {
            listFragment.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onLoadFinished(loader, null);
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
        // TODO: 7/02/17
    }

    @Override
    public void onShiftLongClicked(PeriodWithStableId shift) {
        // TODO: 7/02/17
    }

    @Override
    public int getShiftCount() {
        return mShifts.size();
    }

    @Override
    public PeriodWithStableId getShift(int position) {
        return mShifts.get(position);
    }
}
