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
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

public class RosteredShiftsListFragment extends AbstractShiftListFragment {

    private final Adapter mAdapter = new Adapter();
    private Compliance.Wrapper mCursor = null;

    @Override
    public int getLoaderId() {
        return ShiftListActivity.LOADER_ID_ROSTERED_LIST;
    }

    @Override
    public int getTitle() {
        return R.string.rostered_shifts;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.rosteredShiftsWithComplianceUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = new Compliance.Wrapper(data);
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

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    void addShift(ShiftType shiftType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DateTime minStart;
        if (mCursor != null && mCursor.moveToLast()) {
            minStart = mCursor.getRosteredShift().getEnd().plus(AppConstants.MINIMUM_DURATION_BETWEEN_SHIFTS);
        } else {
            minStart = new DateTime().withTimeAtStartOfDay();
        }
        DateTime newStart = minStart.withTime(shiftTypeCalculator.getStartTime(shiftType, preferences));
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
        values.put(Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START, newStart.getMillis());

        DateTime newEnd = newStart.withTime(shiftTypeCalculator.getEndTime(shiftType, preferences));
        if (!newEnd.isAfter(newStart)) {
            newEnd = newEnd.plusDays(1);
        }
        values.put(Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END, newEnd.getMillis());
        getActivity().getContentResolver().insert(Provider.rosteredShiftsUri, values);
        mAddButtonJustClicked = true;
    }

    private class Adapter extends StableCursorAdapter<Compliance.Wrapper> {

        @Override
        public long getItemId(int position) {
            //noinspection ConstantConditions
            mCursor.moveToPosition(position);
            return mCursor.getId();
        }

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TwoLineViewHolder holder = super.onCreateViewHolder(parent, viewType);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShiftDetailActivity.class);
                    intent.putExtra(ShiftDetailActivity.SHIFT_ID, holder.getItemId());
                    intent.putExtra(ShiftDetailActivity.IS_ROSTERED, true);
                    startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(Provider.rosteredShiftUri(holder.getItemId()), null, null) == 1;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToPosition(position)) {
                Interval rosteredShift = mCursor.getRosteredShift();
                holder.primaryTextView.setText(DateTimeUtils.getFullDateString(rosteredShift.getStart()));
                Interval loggedShift = mCursor.getLoggedShift();
                if (loggedShift == null) {
                    holder.secondaryTextView.setText(DateTimeUtils.getTimeSpanString(rosteredShift));
                } else {
                    holder.secondaryTextView.setText(DateTimeUtils.getDoubleTimeSpanString(rosteredShift, loggedShift));
                }
                int iconResource;
                switch (shiftTypeCalculator.getShiftType(rosteredShift, getActivity())) {
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
                boolean error = AppConstants.hasInsufficientIntervalBetweenShifts(mCursor.getIntervalBetweenShifts()) ||
                        AppConstants.exceedsDurationOverDay(mCursor.getDurationOverDay()) ||
                        AppConstants.exceedsDurationOverWeek(mCursor.getDurationOverWeek()) ||
                        AppConstants.exceedsDurationOverFortnight(mCursor.getDurationOverFortnight()) ||
                        mCursor.consecutiveWeekendsWorked();
                holder.secondaryIconView.setImageResource(error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
            }
        }

    }

}
