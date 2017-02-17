package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.AppConstants;
import com.skepticalone.mecachecker.DurationFormat;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;


public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final String SHIFT_ID = "SHIFT_ID";
    private long mShiftId;
    private final static Calendar sStart = Calendar.getInstance(), sEnd = Calendar.getInstance();
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            mRestBetweenShiftsView,
            mDurationWorkedOverDayView,
            mDurationWorkedOverWeekView,
            mDurationWorkedOverFortnightView,
            mShiftTypeView;
    @ColorInt
    private int mTextColor, mErrorColor;
    private Drawable mErrorDrawable;
    static ShiftDetailFragment create(long id) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mShiftId = getArguments().getLong(SHIFT_ID);
        mTextColor = ResourcesCompat.getColor(getResources(), android.R.color.primary_text_light, null);
        mErrorColor = ResourcesCompat.getColor(getResources(), R.color.colorError, null);
        mErrorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_warning_24dp, null);
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
        mStartTimeView = (TextView) layout.findViewById(R.id.startTime);
        mStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, sStart.getTimeInMillis(), sEnd.getTimeInMillis(), true).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mEndTimeView = (TextView) layout.findViewById(R.id.endTime);
        mEndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, sStart.getTimeInMillis(), sEnd.getTimeInMillis(), false).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mRestBetweenShiftsView = (TextView) layout.findViewById(R.id.rest_between_shifts);
        mDurationWorkedOverDayView = (TextView) layout.findViewById(R.id.duration_worked_over_day);
        mDurationWorkedOverWeekView = (TextView) layout.findViewById(R.id.duration_worked_over_week);
        mDurationWorkedOverFortnightView = (TextView) layout.findViewById(R.id.duration_worked_over_fortnight);
        mShiftTypeView = (TextView) layout.findViewById(R.id.shift_type);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(OverviewActivity.LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftWithComplianceUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            boolean error;
            //
            sStart.setTimeInMillis(cursor.getLong(ComplianceCursor.COLUMN_INDEX_START));
            sEnd.setTimeInMillis(cursor.getLong(ComplianceCursor.COLUMN_INDEX_END));
            mDateView.setText(getString(R.string.date_format, sStart));
            mStartTimeView.setText(getString(R.string.time_format, sStart));
            mEndTimeView.setText(getString(sStart.get(Calendar.DAY_OF_MONTH) == sEnd.get(Calendar.DAY_OF_MONTH) ? R.string.time_format : R.string.time_format_with_day, sEnd));
            //
            long restBetweenShifts = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OF_REST);
            boolean restBetweenShiftsApplicable = restBetweenShifts >= 0;
            error = restBetweenShiftsApplicable && restBetweenShifts < AppConstants.MINIMUM_DURATION_REST;
            mRestBetweenShiftsView.setText(restBetweenShiftsApplicable ? DurationFormat.getDurationString(getActivity(), restBetweenShifts) : getString(R.string.not_applicable));
            mRestBetweenShiftsView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mRestBetweenShiftsView, null, null, error ? mErrorDrawable : null, null);
            //
            long durationOverDay = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_DAY);
            error = durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY;
            mDurationWorkedOverDayView.setText(DurationFormat.getDurationString(getActivity(), durationOverDay));
            mDurationWorkedOverDayView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverDayView, null, null, error ? mErrorDrawable : null, null);
            //
            long durationOverWeek = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_WEEK);
            error = durationOverWeek > AppConstants.MAXIMUM_DURATION_OVER_WEEK;
            mDurationWorkedOverWeekView.setText(DurationFormat.getDurationString(getActivity(), durationOverWeek));
            mDurationWorkedOverWeekView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverWeekView, null, null, error ? mErrorDrawable : null, null);
            //
            long durationOverFortnight = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_FORTNIGHT);
            error = durationOverFortnight > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT;
            mDurationWorkedOverFortnightView.setText(DurationFormat.getDurationString(getActivity(), durationOverFortnight));
            mDurationWorkedOverFortnightView.setTextColor(error ? mErrorColor : mTextColor);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverFortnightView, null, null, error ? mErrorDrawable : null, null);
            //
            if (cursor.isNull(ComplianceCursor.COLUMN_INDEX_WEEKEND_WORKED_START) || cursor.isNull(ComplianceCursor.COLUMN_INDEX_WEEKEND_WORKED_END)) {
                mShiftTypeView.setText(R.string.shift_type_weekday);
            } else {
                mShiftTypeView.setText(getString(R.string.period_format, cursor.getLong(ComplianceCursor.COLUMN_INDEX_WEEKEND_WORKED_START), cursor.getLong(ComplianceCursor.COLUMN_INDEX_WEEKEND_WORKED_END)));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
//
//    interface Listener {
//        //        void onAddShiftClicked();
////        void onShiftClicked(PeriodWithStableId shift);
////        void onShiftSwiped(long shiftId);
////        void onShiftLongClicked(PeriodWithStableId shift);
////        int getShiftCount();
//        PeriodWithStableId getShift(long id);
//    }
}
