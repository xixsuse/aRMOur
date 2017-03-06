package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
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
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class RosteredShiftsListFragment extends AbstractShiftListFragment {

    private final RecyclerView.Adapter mAdapter = new Adapter();
    private boolean mAddButtonJustClicked = false;

    @Override
    int getTitle() {
        return R.string.rostered;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftsWithComplianceUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = new Compliance.Wrapper(data);
        mAdapter.notifyDataSetChanged();
        if (mAddButtonJustClicked) {
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mAddButtonJustClicked = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    void addShift(ShiftType shiftType, @NonNull LocalTime startTime, @NonNull LocalTime endTime) {
        DateTime minStart;
        if (mCursor != null && mCursor.moveToLast()) {
            minStart = mCursor.getRosteredShift().getEnd().plus(AppConstants.MINIMUM_TIME_BETWEEN_SHIFTS);
        } else {
            minStart = new DateTime().withTimeAtStartOfDay();
        }
        DateTime newStart = minStart.withTime(startTime);
        int skipWeekendsKeyId, defaultSkipWeekendsId;
        switch (shiftType) {
            case NORMAL_DAY:
                skipWeekendsKeyId = R.string.key_skip_weekend_normal_day;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_normal_day;
                break;
            case LONG_DAY:
                skipWeekendsKeyId = R.string.key_skip_weekend_long_day;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_long_day;
                break;
            case NIGHT_SHIFT:
                skipWeekendsKeyId = R.string.key_skip_weekend_night_shift;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_night_shift;
                break;
            default:
                throw new IllegalArgumentException();
        }
        boolean skipWeekends = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(getString(skipWeekendsKeyId), getResources().getBoolean(defaultSkipWeekendsId));
        while (newStart.isBefore(minStart) || (skipWeekends && newStart.getDayOfWeek() >= DateTimeConstants.SATURDAY)) {
            newStart = newStart.plusDays(1);
        }
        ContentValues values = new ContentValues();
        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START, newStart.getMillis());

        DateTime newEnd = newStart.withTime(endTime);
        if (!newEnd.isAfter(newStart)) {
            newEnd = newEnd.plusDays(1);
        }
        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_END, newEnd.getMillis());
        getActivity().getContentResolver().insert(ShiftProvider.shiftsUri, values);
        mAddButtonJustClicked = true;
    }

    private class Adapter extends RecyclerView.Adapter<TwoLineViewHolder> {

        Adapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getId();
        }

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onShiftClicked(holder.getItemId());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(ShiftProvider.shiftUri(holder.getItemId()), null, null) == 1;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            int shiftTypeDrawableId;
            switch (mCursor.getShiftType()) {
                case NORMAL_DAY:
                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                    break;
                case LONG_DAY:
                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                    break;
                case NIGHT_SHIFT:
                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                    break;
                case OTHER:
                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            holder.primaryIconView.setImageResource(shiftTypeDrawableId);
            Interval rosteredShift = mCursor.getRosteredShift();
            holder.primaryTextView.setText(getString(R.string.day_date_format, rosteredShift.getStartMillis()));
            Interval loggedShift = mCursor.getLoggedShift();
            if (loggedShift == null) {
                holder.secondaryTextView.setText(getString(R.string.time_span_format, rosteredShift.getStartMillis(), rosteredShift.getEndMillis()));
            } else {
                holder.secondaryTextView.setText(getString(R.string.double_time_span_format, rosteredShift.getStartMillis(), rosteredShift.getEndMillis(), loggedShift.getStartMillis(), loggedShift.getEndMillis()));
            }
            boolean error = AppConstants.hasInsufficientIntervalBetweenShifts(mCursor.getIntervalBetweenShifts()) ||
                    AppConstants.exceedsDurationOverDay(mCursor.getDurationOverDay()) ||
                    AppConstants.exceedsDurationOverWeek(mCursor.getDurationOverWeek()) ||
                    AppConstants.exceedsDurationOverFortnight(mCursor.getDurationOverFortnight()) ||
                    mCursor.consecutiveWeekendsWorked();
            holder.secondaryIconView.setImageResource(error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

    }

}
