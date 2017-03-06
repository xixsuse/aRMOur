package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
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
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.Duration;
import org.joda.time.Interval;

public class RosteredShiftDetailFragment extends AbstractShiftDetailFragment {

    private final Adapter mAdapter = new Adapter();

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    int getLoaderId() {
        return LOADER_ID_ROSTERED_DETAIL;
    }

    @Override
    int getTitle() {
        return R.string.rostered_shift;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.rosteredShiftUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Compliance.Wrapper cursor = new Compliance.Wrapper(c);
        if (cursor.moveToFirst()) {
            final Interval rosteredShift = cursor.getRosteredShift();
            final Interval loggedShift = cursor.getLoggedShift();
            mDateView.setText(getString(R.string.day_date_format, rosteredShift.getStartMillis()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createDatePicker(mShiftId, rosteredShift, loggedShift).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mRosteredStartTimeView.setText(getString(R.string.time_format, rosteredShift.getStartMillis()));
            mRosteredStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, rosteredShift, loggedShift, true, true).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            long dateAtMidnight = rosteredShift.getStart().withTimeAtStartOfDay().getMillis();
            mRosteredEndTimeView.setText(getString(rosteredShift.getEnd().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, rosteredShift.getEndMillis()));
            mRosteredEndTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, rosteredShift, loggedShift, true, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            if (loggedShift == null) {
                mLoggedTimesContainer.setVisibility(View.GONE);
                mToggleLoggedTimesView.setText(R.string.log_hours);
                mToggleLoggedTimesView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START, rosteredShift.getStartMillis());
                        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END, rosteredShift.getEndMillis());
                        getActivity().getContentResolver().update(ShiftProvider.rosteredShiftUri(mShiftId), values, null, null);
                    }
                });
            } else {
                mLoggedStartTimeView.setText(getString(loggedShift.getStart().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, loggedShift.getStartMillis()));
                mLoggedStartTimeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickerFragment.createTimePicker(mShiftId, rosteredShift, loggedShift, false, true).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                    }
                });
                mLoggedEndTimeView.setText(getString(loggedShift.getEnd().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, loggedShift.getEndMillis()));
                mLoggedEndTimeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickerFragment.createTimePicker(mShiftId, rosteredShift, loggedShift, false, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                    }
                });
                mLoggedTimesContainer.setVisibility(View.VISIBLE);
                mToggleLoggedTimesView.setText(R.string.remove_log);
                mToggleLoggedTimesView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.putNull(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START);
                        values.putNull(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END);
                        getActivity().getContentResolver().update(ShiftProvider.rosteredShiftUri(mShiftId), values, null, null);
                    }
                });
            }
            mAdapter.swapCursor(cursor);
        }
    }

    private class Adapter extends CursorAdapter<Compliance.Wrapper> {

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.secondaryIconView.setVisibility(View.GONE);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mCursor != null && mCursor.moveToFirst() ? mCursor.getPreviousWeekend() != null ? 6 : 5 : 0;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToFirst()) {
                switch (position) {
                    case 0:
                        holder.primaryTextView.setText(R.string.shift_type);
                        Interval rosteredShift = mCursor.getRosteredShift();
                        int startTotalMinutes = rosteredShift.getStart().getMinuteOfDay(), endTotalMinutes = rosteredShift.getEnd().getMinuteOfDay();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        if (
                                startTotalMinutes == preferences.getInt(normalDayStartKey, normalDayStartDefault) &&
                                        endTotalMinutes == preferences.getInt(normalDayEndKey, normalDayEndDefault)
                                ) {
                            holder.secondaryTextView.setText(R.string.normal_day);
                            holder.primaryIconView.setImageResource(R.drawable.ic_normal_day_black_24dp);
                        } else if (
                                startTotalMinutes == preferences.getInt(longDayStartKey, longDayStartDefault) &&
                                        endTotalMinutes == preferences.getInt(longDayEndKey, longDayEndDefault)
                                ) {
                            holder.secondaryTextView.setText(R.string.long_day);
                            holder.primaryIconView.setImageResource(R.drawable.ic_long_day_black_24dp);
                        } else if (
                                startTotalMinutes == preferences.getInt(nightShiftStartKey, nightShiftStartDefault) &&
                                        endTotalMinutes == preferences.getInt(nightShiftEndKey, nightShiftEndDefault)
                                ) {
                            holder.secondaryTextView.setText(R.string.night_shift);
                            holder.primaryIconView.setImageResource(R.drawable.ic_night_shift_black_24dp);
                        } else {
                            holder.secondaryTextView.setText(R.string.custom);
                            holder.primaryIconView.setImageResource(R.drawable.ic_custom_shift_black_24dp);
                        }
                        break;
                    case 1:
                        holder.primaryTextView.setText(R.string.time_between_shifts);
                        Interval intervalBetweenShifts = mCursor.getIntervalBetweenShifts();
                        if (intervalBetweenShifts == null) {
                            holder.secondaryTextView.setText(R.string.not_applicable);
                            holder.primaryIconView.setImageResource(0);
                        } else {
                            holder.secondaryTextView.setText(periodFormatter.print(intervalBetweenShifts.toPeriod()));
                            holder.primaryIconView.setImageResource(AppConstants.hasInsufficientIntervalBetweenShifts(intervalBetweenShifts) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        }
                        break;
                    case 2:
                        holder.primaryTextView.setText(R.string.duration_worked_over_day);
                        Duration durationOverDay = mCursor.getDurationOverDay();
                        holder.secondaryTextView.setText(periodFormatter.print(durationOverDay.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverDay(durationOverDay) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        break;
                    case 3:
                        holder.primaryTextView.setText(R.string.duration_worked_over_week);
                        Duration durationOverWeek = mCursor.getDurationOverWeek();
                        holder.secondaryTextView.setText(periodFormatter.print(durationOverWeek.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverWeek(durationOverWeek) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        break;
                    case 4:
                        holder.primaryTextView.setText(R.string.duration_worked_over_fortnight);
                        Duration durationOverFortnight = mCursor.getDurationOverFortnight();
                        holder.secondaryTextView.setText(periodFormatter.print(durationOverFortnight.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverFortnight(durationOverFortnight) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        break;
                    case 5:
                        holder.primaryTextView.setText(R.string.last_weekend_worked);
                        Interval previousWeekend = mCursor.getPreviousWeekend();
                        //noinspection ConstantConditions
                        holder.secondaryTextView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
                        holder.primaryIconView.setImageResource(mCursor.consecutiveWeekendsWorked() ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }

    }

}
