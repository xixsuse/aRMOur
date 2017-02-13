package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;


public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    public static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final String SHIFT_ID = "SHIFT_ID";
    private long mShiftId;
    private long mStart, mEnd;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            maximumHoursPerDayView;

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
        maximumHoursPerDayView = (TextView) layout.findViewById(R.id.maximum_hours_per_day);
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mStart = data.getLong(ComplianceCursor.COLUMN_INDEX_START);
            mEnd = data.getLong(ComplianceCursor.COLUMN_INDEX_END);
            mDateView.setText(getString(R.string.date_format, mStart));
            mStartTimeView.setText(getString(R.string.time_format, mStart));
            mEndTimeView.setText(getString(R.string.time_format_with_day, mEnd));
            maximumHoursPerDayView.setVisibility(data.getInt(ComplianceCursor.COLUMN_INDEX_MAX_HOURS) == 1 ? View.GONE : View.VISIBLE);
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
