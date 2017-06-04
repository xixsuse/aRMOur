package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class AdditionalShiftsListFragment extends AbstractShiftListFragment {

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts._ID,
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
            Contract.AdditionalShifts.COLUMN_NAME_COMMENT,
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
    public int getLoaderId() {
        return ShiftListActivity.LOADER_ID_ADDITIONAL_LIST;
    }

    @Override
    public int getTitle() {
        return R.string.additional_shifts;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    void addShift(ShiftType shiftType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DateTime minStart = new DateTime().withTimeAtStartOfDay();
        if (mCursor != null && mCursor.moveToLast()) {
            DateTime lastShiftEnd = new DateTime(mCursor.getLong(COLUMN_INDEX_END));
            if (lastShiftEnd.isAfter(minStart)) minStart = lastShiftEnd;
        }
        DateTime newStart = minStart.withTime(shiftTypeCalculator.getStartTime(shiftType, preferences));
        while (newStart.isBefore(minStart)) {
            newStart = newStart.plusDays(1);
        }
        ContentValues values = new ContentValues();
        values.put(Contract.AdditionalShifts.COLUMN_NAME_START, newStart.getMillis());
        DateTime newEnd = newStart.withTime(shiftTypeCalculator.getEndTime(shiftType, preferences));
        if (!newEnd.isAfter(newStart)) {
            newEnd = newEnd.plusDays(1);
        }
        values.put(Contract.AdditionalShifts.COLUMN_NAME_END, newEnd.getMillis());
        int hourlyRate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.key_hourly_rate), getResources().getInteger(R.integer.default_hourly_rate));
        values.put(Contract.AdditionalShifts.COLUMN_NAME_RATE, hourlyRate);
        getActivity().getContentResolver().insert(Provider.additionalShiftsUri, values);
        mAddButtonJustClicked = true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.additionalShiftsUri, PROJECTION, null, null, Contract.AdditionalShifts.COLUMN_NAME_START);
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
                    intent.putExtra(ShiftDetailActivity.IS_ADDITIONAL, true);
                    startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(Provider.additionalShiftUri(holder.getItemId()), null, null) == 1;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToPosition(position)) {
                Interval currentShift = new Interval(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                holder.primaryTextView.setText(DateTimeUtils.getFullDateString(currentShift.getStart()));
                String timeSpanString = DateTimeUtils.getTimeSpanString(currentShift);
                if (mCursor.isNull(COLUMN_INDEX_COMMENT)) {
                    holder.secondaryTextView.setText(timeSpanString);
                } else {
                    holder.secondaryTextView.setText(
                            // TODO: 30/04/17 clean up
                            mCursor.getString(COLUMN_INDEX_COMMENT) + '\n' + timeSpanString
                    );
                }
                int iconResource;
                switch (shiftTypeCalculator.getShiftType(currentShift, getActivity())) {
                    case NORMAL_DAY:
                        iconResource = R.drawable.ic_normal_day_black_24dp;
                        break;
                    case LONG_DAY:
                        iconResource = R.drawable.ic_long_day_black_24dp;
                        break;
                    case NIGHT_SHIFT:
                        iconResource = R.drawable.ic_night_shift_black_24dp;
                        break;
                    case OTHER:
                        iconResource = R.drawable.ic_custom_shift_black_24dp;
                        break;
                    default:
                        throw new IllegalStateException();
                }
                holder.primaryIconView.setImageResource(iconResource);
                holder.secondaryIconView.setImageResource(mCursor.isNull(COLUMN_INDEX_PAID) ? mCursor.isNull(COLUMN_INDEX_CLAIMED) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
            }
        }

    }

}
