package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.NonCompliantTimeSpan;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ShiftListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        ShiftAdapter.OnShiftClickListener {

    public static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_FORMATTED_DATE = 3,
            COLUMN_INDEX_FORMATTED_START = 4,
            COLUMN_INDEX_FORMATTED_END = 5;
    private static final int DEFAULT_START_HOUR_OF_DAY = 8;
    private static final int DEFAULT_START_MINUTE = 0;
    private static final int DEFAULT_END_HOUR_OF_DAY = 16;
    private static final int DEFAULT_END_MINUTE = 0;
    private static final String[] COLUMNS = {
            BaseColumns._ID,
            ShiftProvider.START_TIME,
            ShiftProvider.END_TIME,
            ShiftProvider.FORMATTED_DATE,
            ShiftProvider.FORMATTED_START_TIME,
            ShiftProvider.FORMATTED_END_TIME
    };
    private static final String COMPLIANCE_FRAGMENT = "COMPLIANCE_FRAGMENT";
    private static final int LOADER_ID = 0;
    private boolean mTwoPane;
    //    private Cursor mCursor = null;
    private ShiftAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    private TextView mCompliance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shift_list);
        assert recyclerView != null;
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShiftAdapter(this);
        recyclerView.setAdapter(mAdapter);
//        mCompliance = (TextView) findViewById(R.id.compliance_status);
        mTwoPane = findViewById(R.id.shift_detail_container) != null;
        findViewById(R.id.add_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddShiftClicked();
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ShiftProvider.shiftsUri, COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mCursor = data;
//        calculateCompliance();
        mAdapter.swapCursor(data);
        mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onShiftClick(long id) {
        if (mTwoPane) {
            ShiftEditFragment fragment = new ShiftEditFragment();
            Bundle arguments = new Bundle();
            arguments.putLong(ShiftEditFragment.SHIFT_ID, id);
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.shift_detail_container, fragment, ShiftEditFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, ShiftDetailActivity.class);
            intent.putExtra(ShiftEditFragment.SHIFT_ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void onShiftLongClick(long id) {
        getContentResolver().delete(ShiftProvider.shiftUri(id), null, null);
    }

    private void onAddShiftClicked() {
        Calendar start = GregorianCalendar.getInstance();
        Date lastShiftEnd = mAdapter.getLastShiftEnd();
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
    public void showDialogNonCompliantWithMinimumRestHoursBetweenShifts(NonCompliantTimeSpan nonCompliantPeriod) {
        ComplianceFragment.createDialogNonCompliantWithMinimumRestHoursBetweenShifts(nonCompliantPeriod).show(getFragmentManager(), COMPLIANCE_FRAGMENT);
    }

    @Override
    public void showDialogNonCompliantWithMaximumHoursPerDay(NonCompliantTimeSpan nonCompliantPeriod) {
        ComplianceFragment.createDialogNonCompliantWithMaximumHoursPerDay(nonCompliantPeriod).show(getFragmentManager(), COMPLIANCE_FRAGMENT);
    }

    @Override
    public void showDialogNonCompliantWithMaximumHoursPerWeek(NonCompliantTimeSpan nonCompliantPeriod) {
        ComplianceFragment.createDialogNonCompliantWithMaximumHoursPerWeek(nonCompliantPeriod).show(getFragmentManager(), COMPLIANCE_FRAGMENT);
    }

    @Override
    public void showDialogNonCompliantWithMaximumHoursPerFortnight(NonCompliantTimeSpan nonCompliantPeriod) {
        ComplianceFragment.createDialogNonCompliantWithMaximumHoursPerFortnight(nonCompliantPeriod).show(getFragmentManager(), COMPLIANCE_FRAGMENT);
    }

    @Override
    public void showDialogNonCompliantWithMaximumConsecutiveWeekends() {
        ComplianceFragment.createDialogNonCompliantWithMaximumConsecutiveWeekends().show(getFragmentManager(), COMPLIANCE_FRAGMENT);
    }
}
