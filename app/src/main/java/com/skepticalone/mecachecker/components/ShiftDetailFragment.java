package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_DETAIL_ID = 1;
    private final static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendYears()
            .appendSuffix(" year", " years")
            .appendSeparator(", ")
            .appendMonths()
            .appendSuffix(" month", " months")
            .appendSeparator(", ")
            .appendWeeks()
            .appendSuffix(" week", " weeks")
            .appendSeparator(", ")
            .appendDays()
            .appendSuffix(" day", " days")
            .appendSeparator(", ")
            .appendHours()
            .appendSuffix(" hour", " hours")
            .appendSeparator(", ")
            .appendMinutes()
            .appendSuffix(" minute", " minutes")
            .toFormatter();
    private long mShiftId;
    private TextView
            mDateView,
            mRosteredStartTimeView,
            mRosteredEndTimeView,
            mLoggedStartTimeView,
            mLoggedEndTimeView,
            mToggleLoggedTimesView;
    private View mLoggedTimesContainer;
    private RecyclerView mRecyclerView;

    static ShiftDetailFragment create(long id) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftId = getArguments().getLong(ShiftDetailActivity.SHIFT_ID, ShiftDetailActivity.NO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mRosteredStartTimeView = (TextView) layout.findViewById(R.id.rostered_start_time);
        mRosteredEndTimeView = (TextView) layout.findViewById(R.id.rostered_end_time);
        mLoggedTimesContainer = layout.findViewById(R.id.logged_times);
        mLoggedStartTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_start_time);
        mLoggedEndTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_end_time);
        mToggleLoggedTimesView = (TextView) layout.findViewById(R.id.toggle_logged_times);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftWithComplianceUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ComplianceCursor cursor = new ComplianceCursor(c);
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
                        values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START, rosteredShift.getStartMillis());
                        values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END, rosteredShift.getEndMillis());
                        getActivity().getContentResolver().update(ShiftProvider.shiftUri(mShiftId), values, null, null);
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
                        values.putNull(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START);
                        values.putNull(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END);
                        getActivity().getContentResolver().update(ShiftProvider.shiftUri(mShiftId), values, null, null);
                    }
                });
            }
            mRecyclerView.setAdapter(new Adapter(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class Adapter extends RecyclerView.Adapter<TwoLineViewHolder> {

        private final ComplianceCursor mCursor;

        Adapter(ComplianceCursor c) {
            super();
            mCursor = c;
            mCursor.moveToFirst();
            notifyDataSetChanged();
        }

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.secondaryIconView.setVisibility(View.GONE);
            return holder;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.primaryTextView.setText(R.string.shift_type);
                    int shiftTypeDrawableId, shiftTypeStringId;
                    switch (mCursor.getShiftType()) {
                        case NORMAL_DAY:
                            shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                            shiftTypeStringId = R.string.normal_day;
                            break;
                        case LONG_DAY:
                            shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                            shiftTypeStringId = R.string.long_day;
                            break;
                        case NIGHT_SHIFT:
                            shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                            shiftTypeStringId = R.string.night_shift;
                            break;
                        case OTHER:
                            shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                            shiftTypeStringId = R.string.custom;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    holder.secondaryTextView.setText(shiftTypeStringId);
                    holder.primaryIconView.setImageResource(shiftTypeDrawableId);
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

        @Override
        public int getItemCount() {
            return (mCursor != null && mCursor.moveToFirst()) ? mCursor.getPreviousWeekend() != null ? 6 : 5 : 0;
        }
    }
}
