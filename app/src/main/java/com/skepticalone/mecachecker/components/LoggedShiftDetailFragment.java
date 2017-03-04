package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;

import org.joda.time.Interval;


public class LoggedShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] PROJECTION = new String[]{
            ShiftContract.RosteredShift._ID,
            ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START,
            ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_END,
            ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START,
            ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END
    };
    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_SCHEDULED_START = 1,
            COLUMN_INDEX_SCHEDULED_END = 2,
            COLUMN_INDEX_LOGGED_START = 3,
            COLUMN_INDEX_LOGGED_END = 4;

    private static final int LOADER_DETAIL_ID = 6;
    private long mShiftId;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            mLoggedStartTimeView,
            mLoggedEndTimeView,
            mToggleLoggedTimesView;
    private View mLoggedTimesContainer;
    //    ,
//            mShiftTypeView,
//            mTimeBetweenShiftsView,
//            mDurationWorkedOverDayView,
//            mDurationWorkedOverWeekView,
//            mDurationWorkedOverFortnightView,
//            mCurrentWeekendView,
//            mLastWeekendWorkedLabelView,
//            mLastWeekendWorkedView;

    static LoggedShiftDetailFragment create(long id) {
        LoggedShiftDetailFragment fragment = new LoggedShiftDetailFragment();
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
        View layout = inflater.inflate(R.layout.logged_shift_detail_fragment, container, false);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mStartTimeView = (TextView) layout.findViewById(R.id.rostered_start_time);
        mEndTimeView = (TextView) layout.findViewById(R.id.rostered_end_time);
        mLoggedTimesContainer = layout.findViewById(R.id.logged_times);
        mLoggedStartTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_start_time);
        mLoggedEndTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_end_time);
        mToggleLoggedTimesView = (TextView) layout.findViewById(R.id.toggle_logged_times);
//        mShiftTypeView = (TextView) layout.findViewById(R.id.shift_type);
//        mTimeBetweenShiftsView = (TextView) layout.findViewById(R.id.time_between_shifts);
//        mDurationWorkedOverDayView = (TextView) layout.findViewById(R.id.duration_worked_over_day);
//        mDurationWorkedOverWeekView = (TextView) layout.findViewById(R.id.duration_worked_over_week);
//        mDurationWorkedOverFortnightView = (TextView) layout.findViewById(R.id.duration_worked_over_fortnight);
//        mCurrentWeekendView = (TextView) layout.findViewById(R.id.current_weekend);
//        mLastWeekendWorkedLabelView = (TextView) layout.findViewById(R.id.last_weekend_worked_label);
//        mLastWeekendWorkedView = (TextView) layout.findViewById(R.id.last_weekend_worked);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftUri(mShiftId), PROJECTION, null, null, ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            final Interval rosteredShift = new Interval(cursor.getLong(COLUMN_INDEX_SCHEDULED_START), cursor.getLong(COLUMN_INDEX_SCHEDULED_END));
            final Interval loggedShift = (cursor.isNull(COLUMN_INDEX_LOGGED_START) || cursor.isNull(COLUMN_INDEX_LOGGED_END)) ?
                    null :
                    new Interval(cursor.getLong(COLUMN_INDEX_LOGGED_START), cursor.getLong(COLUMN_INDEX_LOGGED_END));
            mDateView.setText(getString(R.string.day_date_format, rosteredShift.getStartMillis()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createDatePicker(mShiftId, rosteredShift, loggedShift).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mStartTimeView.setText(getString(R.string.time_format, rosteredShift.getStartMillis()));
            mStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, rosteredShift, loggedShift, true, true).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            long dateAtMidnight = rosteredShift.getStart().withTimeAtStartOfDay().getMillis();
            mEndTimeView.setText(getString(rosteredShift.getEnd().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, rosteredShift.getEndMillis()));
            mEndTimeView.setOnClickListener(new View.OnClickListener() {
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
//            int shiftTypeDrawableId, shiftTypeStringId;
//            switch (cursor.getShiftCategory()) {
//                case ComplianceCursor.SHIFT_TYPE_NORMAL_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
//                    shiftTypeStringId = R.string.normal_day;
//                    break;
//                case ComplianceCursor.SHIFT_TYPE_LONG_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
//                    shiftTypeStringId = R.string.long_day;
//                    break;
//                case ComplianceCursor.SHIFT_TYPE_NIGHT_SHIFT:
//                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
//                    shiftTypeStringId = R.string.night_shift;
//                    break;
//                default:
//                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
//                    shiftTypeStringId = R.string.custom;
//                    break;
//            }
//            mShiftTypeView.setText(shiftTypeStringId);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mShiftTypeView, 0, 0, shiftTypeDrawableId, 0);
//            Duration duration = cursor.getTimeBetweenShifts();
//            if (duration == null) {
//                mTimeBetweenShiftsView.setText(R.string.not_applicable);
//                mTimeBetweenShiftsView.setTextColor(mTextColor);
//                mTimeBetweenShiftsView.setCompoundDrawables(null, null, null, null);
//            } else {
//                mTimeBetweenShiftsView.setText(periodFormatter.print(duration.toPeriodTo(shift.getStart())));
//                error = AppConstants.hasInsufficientTimeBetweenShifts(duration);
//                mTimeBetweenShiftsView.setTextColor(error ? mErrorColor : mTextColor);
//                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mTimeBetweenShiftsView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            }
//            duration = cursor.getDurationOverDay();
//            mDurationWorkedOverDayView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverDay(duration);
//            mDurationWorkedOverDayView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverDayView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            duration = cursor.getDurationOverWeek();
//            mDurationWorkedOverWeekView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverWeek(duration);
//            mDurationWorkedOverWeekView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverWeekView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            duration = cursor.getDurationOverFortnight();
//            mDurationWorkedOverFortnightView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverFortnight(duration);
//            mDurationWorkedOverFortnightView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverFortnightView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            Interval currentWeekend = cursor.getCurrentWeekend();
//            if (currentWeekend != null) {
//                mCurrentWeekendView.setText(getString(R.string.period_format, currentWeekend.getStartMillis(), currentWeekend.getEndMillis() - 1));
//                mLastWeekendWorkedLabelView.setVisibility(View.VISIBLE);
//                Interval previousWeekend = cursor.getPreviousWeekend();
//                if (previousWeekend != null) {
//                    mLastWeekendWorkedView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
//                    error = cursor.consecutiveWeekendsWorked();
//                    mLastWeekendWorkedView.setTextColor(error ? mErrorColor : mTextColor);
//                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mLastWeekendWorkedView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//                } else {
//                    mLastWeekendWorkedView.setText(R.string.not_applicable);
//                    mLastWeekendWorkedView.setTextColor(mTextColor);
//                    mLastWeekendWorkedView.setCompoundDrawables(null, null, null, null);
//                }
//                mLastWeekendWorkedView.setVisibility(View.VISIBLE);
//            } else {
//                mCurrentWeekendView.setText(R.string.not_applicable);
//                mLastWeekendWorkedLabelView.setVisibility(View.GONE);
//                mLastWeekendWorkedView.setVisibility(View.GONE);
//            }
        }
    }
}
