package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.GregorianCalendar;

public class ShiftListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        ShiftAdapter.OnShiftClickListener {

    private static final int LOADER_ID = 0;
    private boolean mTwoPane;

    private ShiftAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shift_list);
        assert recyclerView != null;
        mAdapter = new ShiftAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mTwoPane = findViewById(R.id.shift_detail_container) != null;
        findViewById(R.id.add_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(ShiftProvider.START_TIME, new GregorianCalendar(2016, 3, 24, 8, 10).getTimeInMillis() / 1000);
                values.put(ShiftProvider.END_TIME, new GregorianCalendar(2016, 3, 24, 14, 50).getTimeInMillis() / 1000);
                getContentResolver().insert(ShiftProvider.shiftsUri, values);
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ShiftProvider.shiftsUri, ShiftAdapter.COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onShiftClick(long id) {
        if (mTwoPane) {
            ShiftDetailFragment fragment = new ShiftDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putLong(ShiftDetailFragment.SHIFT_ID, id);
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.shift_detail_container, fragment, ShiftDetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, ShiftDetailActivity.class);
            intent.putExtra(ShiftDetailFragment.SHIFT_ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void onShiftLongClick(long id) {
        getContentResolver().delete(ShiftProvider.shiftUri(id), null, null);
    }

}
