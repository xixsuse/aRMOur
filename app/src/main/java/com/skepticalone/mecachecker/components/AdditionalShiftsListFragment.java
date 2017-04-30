package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Intent;
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

import com.skepticalone.mecachecker.BuildConfig;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class AdditionalShiftsListFragment extends AbstractShiftListFragment {

    private static final String[] PROJECTION = new String[]{
            ShiftContract.AdditionalShifts._ID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_START,
            ShiftContract.AdditionalShifts.COLUMN_NAME_END,
            ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            ShiftContract.AdditionalShifts.COLUMN_NAME_PAID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            COLUMN_INDEX_COMMENT = 5;
    private final Adapter mAdapter = new Adapter();
    private Cursor mCursor = null;

    @Override
    int getLoaderId() {
        return LOADER_ID_ADDITIONAL_LIST;
    }

    @Override
    int getTitle() {
        return R.string.additional_shifts;
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
        int hourlyRate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.key_hourly_rate), getResources().getInteger(R.integer.default_hourly_rate));
        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_RATE, hourlyRate);
        // TODO: 30/04/17 remove this
        if (BuildConfig.DEBUG) {
            values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT, "This is a comment");
        }
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
        mAdapter.swapCursor(mCursor);
        if (mAddButtonJustClicked) {
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mAddButtonJustClicked = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mAdapter.swapCursor(null);
    }

    private class Adapter extends StableCursorAdapter<Cursor> {

        @Override
        public long getItemId(int position) {
            //noinspection ConstantConditions
            mCursor.moveToPosition(position);
            return mCursor.getLong(COLUMN_INDEX_ID);
        }

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShiftDetailActivity.class);
                    intent.putExtra(ShiftDetailActivity.SHIFT_ID, holder.getItemId());
                    intent.putExtra(ShiftDetailActivity.IS_ROSTERED, false);
                    startActivity(intent);
                }
            });
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
            if (mCursor != null && mCursor.moveToPosition(position)) {
                Interval currentShift = new Interval(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                holder.primaryTextView.setText(DateTimeUtils.getFullDateString(currentShift.getStart()));
                // TODO: 30/04/17 clean up
                if (mCursor.isNull(COLUMN_INDEX_COMMENT)) {
                    holder.secondaryTextView.setText(DateTimeUtils.getTimeSpanString(currentShift));
                } else {
                    holder.secondaryTextView.setText(
                            mCursor.getString(COLUMN_INDEX_COMMENT) + '\n' +
                                    DateTimeUtils.getTimeSpanString(currentShift)
                    );
                }
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
                holder.secondaryIconView.setImageResource(mCursor.isNull(COLUMN_INDEX_PAID) ? mCursor.isNull(COLUMN_INDEX_CLAIMED) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
            }
        }

    }

}
