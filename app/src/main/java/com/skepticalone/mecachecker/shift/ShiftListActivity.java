package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract.Shift;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.GregorianCalendar;

public class ShiftListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private static final int LOADER_ID = 0;
    private static final String[] COLUMNS = {
            Shift._ID,
            Shift.START_AS_DATE,
            Shift.START_AS_TIME,
            Shift.END_AS_TIME
    };
    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_DATE = 1,
            COLUMN_INDEX_START = 2,
            COLUMN_INDEX_END = 3;
    private boolean mTwoPane;

    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shift_list);
        assert recyclerView != null;
        mAdapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
        mTwoPane = findViewById(R.id.shift_detail_container) != null;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ShiftProvider.shiftsUri, COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void addShift(View v) {
        ContentValues values = new ContentValues();
        values.put(Shift.COLUMN_NAME_START, new GregorianCalendar(2016, 3, 24, 8, 10).getTimeInMillis() / 1000);
        values.put(Shift.COLUMN_NAME_END, new GregorianCalendar(2016, 3, 24, 14, 50).getTimeInMillis() / 1000);
        getContentResolver().insert(ShiftProvider.shiftsUri, values);
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

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private Cursor mCursor = null;

        SimpleItemRecyclerViewAdapter() {
            setHasStableIds(true);
        }

        void swapCursor(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(COLUMN_INDEX_ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shift_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            final long id = mCursor.getLong(COLUMN_INDEX_ID);
            holder.dateView.setText(mCursor.getString(COLUMN_INDEX_DATE));
            holder.startView.setText(mCursor.getString(COLUMN_INDEX_START));
            holder.endView.setText(mCursor.getString(COLUMN_INDEX_END));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        ShiftDetailFragment fragment = new ShiftDetailFragment();
                        Bundle arguments = new Bundle();
                        arguments.putLong(ShiftDetailFragment.SHIFT_ID, id);
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.shift_detail_container, fragment, ShiftDetailFragment.TAG)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ShiftDetailActivity.class);
                        intent.putExtra(ShiftDetailFragment.SHIFT_ID, id);
                        context.startActivity(intent);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getContentResolver().delete(ShiftProvider.shiftUri(id), null, null);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView
                    dateView,
                    startView,
                    endView;
            ViewHolder(View view) {
                super(view);
                dateView = (TextView) view.findViewById(R.id.date);
                startView = (TextView) view.findViewById(R.id.start);
                endView = (TextView) view.findViewById(R.id.end);
            }
        }
    }
}
