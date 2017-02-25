package com.skepticalone.mecachecker.components;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DurationFormat;

import java.util.Calendar;

public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final long NO_ID = -1L;
    static final String SHIFT_ID = "SHIFT_ID";
    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final int LOADER_DETAIL_ID = 1;
    private final static Calendar sStart = Calendar.getInstance(), sEnd = Calendar.getInstance();
    private long mShiftId;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            mShiftTypeView,
            mRestBetweenShiftsView,
            mDurationWorkedOverDayView,
            mDurationWorkedOverWeekView,
            mDurationWorkedOverFortnightView,
            mCurrentWeekendView,
            mLastWeekendWorkedLabelView,
            mLastWeekendWorkedView;

    @ColorInt
    private int mTextColor, mErrorColor;

    static ShiftDetailFragment create(long id) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftId = getArguments().getLong(SHIFT_ID, NO_ID);
        mTextColor = ContextCompat.getColor(getActivity(), android.R.color.primary_text_light);
        mErrorColor = ContextCompat.getColor(getActivity(), R.color.colorError);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.create(mShiftId, sStart.getTimeInMillis(), sEnd.getTimeInMillis()).show(getFragmentManager(), DATE_PICKER_FRAGMENT);
            }
        });
        mStartTimeView = (TextView) layout.findViewById(R.id.start_time);
        mStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, sStart.getTimeInMillis(), sEnd.getTimeInMillis(), true).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mEndTimeView = (TextView) layout.findViewById(R.id.end_time);
        mEndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, sStart.getTimeInMillis(), sEnd.getTimeInMillis(), false).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mShiftTypeView = (TextView) layout.findViewById(R.id.shift_type);
        mRestBetweenShiftsView = (TextView) layout.findViewById(R.id.rest_between_shifts);
        mDurationWorkedOverDayView = (TextView) layout.findViewById(R.id.duration_worked_over_day);
        mDurationWorkedOverWeekView = (TextView) layout.findViewById(R.id.duration_worked_over_week);
        mDurationWorkedOverFortnightView = (TextView) layout.findViewById(R.id.duration_worked_over_fortnight);
        mCurrentWeekendView = (TextView) layout.findViewById(R.id.current_weekend);
        mLastWeekendWorkedLabelView = (TextView) layout.findViewById(R.id.last_weekend_worked_label);
        mLastWeekendWorkedView = (TextView) layout.findViewById(R.id.last_weekend_worked);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ComplianceCursor cursor = new ComplianceCursor(c);
        if (cursor.moveToFirst()) {
            boolean error;
            //
            sStart.setTimeInMillis(cursor.getStart());
            sEnd.setTimeInMillis(cursor.getEnd());
            mDateView.setText(getString(R.string.day_date_format, sStart));
            mStartTimeView.setText(getString(R.string.time_format, sStart));
            mEndTimeView.setText(getString(sStart.get(Calendar.DAY_OF_MONTH) == sEnd.get(Calendar.DAY_OF_MONTH) ? R.string.time_format : R.string.time_format_with_day, sEnd));
            //
            int shiftTypeDrawableId, shiftTypeStringId;
            switch (cursor.getShiftType()) {
                case ComplianceCursor.SHIFT_TYPE_NORMAL_DAY:
                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                    shiftTypeStringId = R.string.normal_day;
                    break;
                case ComplianceCursor.SHIFT_TYPE_LONG_DAY:
                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                    shiftTypeStringId = R.string.long_day;
                    break;
                case ComplianceCursor.SHIFT_TYPE_NIGHT_SHIFT:
                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                    shiftTypeStringId = R.string.night_shift;
                    break;
                default:
                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                    shiftTypeStringId = R.string.custom;
                    break;
            }
            mShiftTypeView.setText(shiftTypeStringId);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mShiftTypeView, 0, 0, shiftTypeDrawableId, 0);
            //
            long restBetweenShifts = cursor.getDurationOfRest();
            boolean restBetweenShiftsApplicable = restBetweenShifts >= 0;
            error = restBetweenShiftsApplicable && restBetweenShifts < AppConstants.MINIMUM_DURATION_REST;
            mRestBetweenShiftsView.setText(restBetweenShiftsApplicable ? DurationFormat.getDurationString(getActivity(), restBetweenShifts) : getString(R.string.not_applicable));
            mRestBetweenShiftsView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mRestBetweenShiftsView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
            //
            long durationOverDay = cursor.getDurationOverDay();
            error = durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY;
            mDurationWorkedOverDayView.setText(DurationFormat.getDurationString(getActivity(), durationOverDay));
            mDurationWorkedOverDayView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverDayView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
            //
            long durationOverWeek = cursor.getDurationOverWeek();
            error = durationOverWeek > AppConstants.MAXIMUM_DURATION_OVER_WEEK;
            mDurationWorkedOverWeekView.setText(DurationFormat.getDurationString(getActivity(), durationOverWeek));
            mDurationWorkedOverWeekView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverWeekView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
            //
            long durationOverFortnight = cursor.getDurationOverFortnight();
            error = durationOverFortnight > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT;
            mDurationWorkedOverFortnightView.setText(DurationFormat.getDurationString(getActivity(), durationOverFortnight));
            mDurationWorkedOverFortnightView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverFortnightView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
            //
            if (cursor.isWeekend()) {
                mCurrentWeekendView.setText(getString(R.string.period_format, cursor.getCurrentWeekendStart(), cursor.getCurrentWeekendEnd() - 1));
                mLastWeekendWorkedLabelView.setVisibility(View.VISIBLE);
                if (cursor.previousWeekendWorked()){
                    mLastWeekendWorkedView.setText(getString(R.string.period_format, cursor.getPreviousWeekendWorkedStart(), cursor.getPreviousWeekendWorkedEnd() - 1));
                    error = cursor.consecutiveWeekendsWorked();
                    mLastWeekendWorkedView.setTextColor(error ? mErrorColor : mTextColor);
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mLastWeekendWorkedView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
                } else {
                    mLastWeekendWorkedView.setText(R.string.not_applicable);
                    mLastWeekendWorkedView.setTextColor(mTextColor);
                    mLastWeekendWorkedView.setCompoundDrawables(null, null, null, null);
                }
                mLastWeekendWorkedView.setVisibility(View.VISIBLE);
            } else {
                mCurrentWeekendView.setText(R.string.not_applicable);
                mLastWeekendWorkedLabelView.setVisibility(View.GONE);
                mLastWeekendWorkedView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}