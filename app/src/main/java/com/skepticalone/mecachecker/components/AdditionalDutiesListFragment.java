package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class AdditionalDutiesListFragment extends AbstractShiftListFragment {

    private static final int LOADER_ID = 2;
    private static final String[] PROJECTION = new String[]{
            ShiftContract.AdditionalShifts._ID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_START,
            ShiftContract.AdditionalShifts.COLUMN_NAME_END,
            ShiftContract.AdditionalShifts.COLUMN_NAME_RATE,
            ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            ShiftContract.AdditionalShifts.COLUMN_NAME_PAID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_RATE = 3,
            COLUMN_INDEX_CLAIMED = 4,
            COLUMN_INDEX_PAID = 5,
            COLUMN_INDEX_COMMENT = 6;
    private final RecyclerView.Adapter mAdapter = new Adapter();
    private Cursor mCursor = null;

    @Override
    int getLoaderId() {
        return LOADER_ID;
    }

    @Override
    int getTitle() {
        return R.string.additional;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    void addShift(ShiftType shiftType, @NonNull LocalTime startTime, @NonNull LocalTime endTime) {
        DateTime minStart = new DateTime().withTimeAtStartOfDay();
        if (mCursor != null && mCursor.moveToLast()) {
            DateTime lastShiftEnd = new DateTime(mCursor.getLong(COLUMN_INDEX_END));
            if (lastShiftEnd.isAfter(minStart)) minStart = lastShiftEnd;
        }
        DateTime newStart = minStart.withTime(startTime);
        while (newStart.isBefore(minStart)) {
            newStart = newStart.plusDays(1);
        }
        ContentValues values = new ContentValues();
        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_START, newStart.getMillis());
        DateTime newEnd = newStart.withTime(endTime);
        if (!newEnd.isAfter(newStart)) {
            newEnd = newEnd.plusDays(1);
        }
        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_END, newEnd.getMillis());
        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_RATE, getResources().getInteger(R.integer.additional_duties_rate_house_officer));
        getActivity().getContentResolver().insert(ShiftProvider.additionalShiftsUri, values);
        mAddButtonJustClicked = true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.additionalShiftsUri, PROJECTION, null, null, ShiftContract.AdditionalShifts.COLUMN_NAME_START);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter.notifyDataSetChanged();
        if (mAddButtonJustClicked) {
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mAddButtonJustClicked = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mAdapter.notifyDataSetChanged();
    }

    private class Adapter extends RecyclerView.Adapter<TwoLineViewHolder> {

        Adapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(COLUMN_INDEX_ID);
        }

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(ShiftProvider.additionalShiftUri(holder.getItemId()), null, null) == 1;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            Interval currentShift = new Interval(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
            holder.primaryTextView.setText(getString(R.string.day_date_format, currentShift.getStartMillis()));
            holder.secondaryTextView.setText(getString(R.string.time_span_format, currentShift.getStartMillis(), currentShift.getEndMillis()));
            int startTotalMinutes = currentShift.getStart().getMinuteOfDay(), endTotalMinutes = currentShift.getEnd().getMinuteOfDay();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            holder.primaryIconView.setImageResource(
                    startTotalMinutes == preferences.getInt(normalDayStartKey, normalDayStartDefault) &&
                            endTotalMinutes == preferences.getInt(normalDayEndKey, normalDayEndDefault) ?
                            R.drawable.ic_normal_day_black_24dp :
                            startTotalMinutes == preferences.getInt(longDayStartKey, longDayStartDefault) &&
                                    endTotalMinutes == preferences.getInt(longDayEndKey, longDayEndDefault) ?
                                    R.drawable.ic_long_day_black_24dp :
                                    startTotalMinutes == preferences.getInt(nightShiftStartKey, nightShiftStartDefault) &&
                                            endTotalMinutes == preferences.getInt(nightShiftEndKey, nightShiftEndDefault) ?
                                            R.drawable.ic_night_shift_black_24dp :
                                            R.drawable.ic_custom_shift_black_24dp
            );
            holder.secondaryIconView.setImageResource(mCursor.isNull(COLUMN_INDEX_CLAIMED) ? R.drawable.ic_check_box_outline_blank_black_24dp : mCursor.isNull(COLUMN_INDEX_PAID) ? R.drawable.ic_indeterminate_check_box_black_24dp : R.drawable.ic_check_box_black_24dp);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

    }

}
