package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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


public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @SuppressWarnings("WeakerAccess")
    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    @SuppressWarnings("WeakerAccess")
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final String SHIFT_ID = "SHIFT_ID";
    private long mShiftId;
    private long mStart, mEnd;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            mDurationWorkedOverDayView,
            mDurationWorkedOverWeekView,
            mDurationWorkedOverFortnightView;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.create(mShiftId, mStart, mEnd).show(getFragmentManager(), DATE_PICKER_FRAGMENT);
            }
        });
        mStartTimeView = (TextView) layout.findViewById(R.id.startTime);
        mStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, mStart, mEnd, true).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mEndTimeView = (TextView) layout.findViewById(R.id.endTime);
        mEndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment.create(mShiftId, mStart, mEnd, false).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        mDurationWorkedOverDayView = (TextView) layout.findViewById(R.id.duration_worked_over_day);
        mDurationWorkedOverWeekView = (TextView) layout.findViewById(R.id.duration_worked_over_week);
        mDurationWorkedOverFortnightView = (TextView) layout.findViewById(R.id.duration_worked_over_fortnight);
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
            mStart = cursor.getLong(ComplianceCursor.COLUMN_INDEX_START);
            mEnd = cursor.getLong(ComplianceCursor.COLUMN_INDEX_END);
            mDateView.setText(getString(R.string.date_format, mStart));
            mStartTimeView.setText(getString(R.string.time_format, mStart));
            mEndTimeView.setText(getString(R.string.time_format_with_day, mEnd));
            //
            long durationOverDay = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_DAY);
            mDurationWorkedOverDayView.setText(DurationFormat.getDurationString(getActivity(), durationOverDay));
            mDurationWorkedOverDayView.setTextColor(ResourcesCompat.getColor(getResources(), durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY ? R.color.colorError : R.color.colorPrimary, null));
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverDayView, null, null, durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_warning_24dp, null) : null, null);
            //
            long durationOverWeek = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_WEEK);
            mDurationWorkedOverWeekView.setText(DurationFormat.getDurationString(getActivity(), durationOverWeek));
            mDurationWorkedOverWeekView.setTextColor(ResourcesCompat.getColor(getResources(), durationOverWeek > AppConstants.MAXIMUM_DURATION_OVER_WEEK ? R.color.colorError : R.color.colorPrimary, null));
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverWeekView, null, null, durationOverWeek > AppConstants.MAXIMUM_DURATION_OVER_WEEK ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_warning_24dp, null) : null, null);
            //
            long durationOverFortnight = cursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_FORTNIGHT);
            mDurationWorkedOverFortnightView.setText(DurationFormat.getDurationString(getActivity(), durationOverFortnight));
            mDurationWorkedOverFortnightView.setTextColor(ResourcesCompat.getColor(getResources(), durationOverFortnight > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT ? R.color.colorError : R.color.colorPrimary, null));
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverFortnightView, null, null, durationOverFortnight > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_warning_24dp, null) : null, null);
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
