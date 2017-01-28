package com.skepticalone.mecachecker.shift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.skepticalone.mecachecker.BuildConfig;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;

import java.util.Calendar;

public class ShiftDetailFragment
        extends
        Fragment
        implements
        TimePickerFragment.OnShiftTimeSetListener,
        SeekBar.OnSeekBarChangeListener,
        Shift.ShiftDisplayListener
{
    public static final String SHIFT_ID = "SHIFT_ID";
    static final String TAG = "SHIFT_DETAIL_FRAGMENT";
    static final long NO_ID = -1;
    private static final String[] COLUMNS = {
            ShiftContract.Shift.COLUMN_NAME_START,
            ShiftContract.Shift.COLUMN_NAME_END
    };
    private static final int
            COLUMN_INDEX_START = 0,
            COLUMN_INDEX_END = 1;
    private static final String SELECTION = ShiftContract.Shift._ID + "=?";
    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private TextView mDateView, mStartTimeView, mEndTimeView, mDurationView;
    private SeekBar mDurationBar;
    private Shift mShift;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        View dateSection = layout.findViewById(R.id.dateSection);
        dateSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.create(mShift).show(getFragmentManager(), DATE_PICKER_FRAGMENT);
            }
        });
        mDateView = (TextView) dateSection.findViewById(R.id.date);
        View startSection = layout.findViewById(R.id.startSection);
        startSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerFragment(true);
            }
        });
        mStartTimeView = (TextView) startSection.findViewById(R.id.startTime);
        View endSection = layout.findViewById(R.id.endSection);
        endSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerFragment(false);
            }
        });
        mEndTimeView = (TextView) endSection.findViewById(R.id.endTime);
        mDurationView = (TextView) layout.findViewById(R.id.duration);
        mDurationBar = (SeekBar) layout.findViewById(R.id.durationBar);
        if (BuildConfig.DEBUG && (mDateView == null || mStartTimeView == null || mEndTimeView == null || mDurationView == null || mDurationBar == null)) {
            throw new AssertionError();
        }
        mDurationBar.setOnSeekBarChangeListener(this);
        return layout;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Cursor cursor = new ShiftDbHelper(getActivity()).getReadableDatabase().query(ShiftContract.Shift.TABLE_NAME,
//                COLUMNS,
//                SELECTION,
//                new String[]{Long.toString(getArguments().getLong(SHIFT_ID, NO_ID))},
//                null,
//                null,
//                null
//        );
//        if (cursor.moveToFirst()) {
//            Calendar start = new GregorianCalendar();
//            start.setTimeInMillis(cursor.getLong(COLUMN_INDEX_START) * 1000);
//            Calendar end = new GregorianCalendar();
//            end.setTimeInMillis(cursor.getLong(COLUMN_INDEX_END) * 1000);
//            mShift = new Shift(this, start, end);
//        }
//        cursor.close();
//    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int steps, boolean fromUser) {
        if (fromUser) mShift.onDurationUpdatedByUser(steps);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void showTimePickerFragment(boolean isStart) {
        TimePickerFragment.create(isStart, mShift).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
    }

    public void onDateSet(int year, int month, int dayOfMonth) {
        mShift.updateDate(year, month, dayOfMonth);
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        mShift.updateStart(hourOfDay, minute);
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        mShift.updateEnd(hourOfDay, minute);
    }

    @Override
    public void updateDate(Calendar date) {
        mDateView.setText(getString(R.string.date_format, date));
    }

    @Override
    public void updateStart(Calendar start) {
        mStartTimeView.setText(getString(R.string.time_format, start));
    }

    @Override
    public void updateEnd(Calendar end, boolean sameDay) {
        mEndTimeView.setText(getString(sameDay ? R.string.time_format : R.string.time_format_with_day, end));
    }

    @Override
    public void updateDuration(int hours, int minutes) {
        String duration;
        if (hours > 0){
            String hoursString = getResources().getQuantityString(R.plurals.hours, hours, hours);
            if (minutes > 0){
                String minutesString = getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
                duration = getString(R.string.hours_and_minutes, hoursString, minutesString);
            } else {
                duration = hoursString;
            }
        } else {
            duration = getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
        }
        mDurationView.setText(duration);
    }

    @Override
    public void updateDurationBarProgress(int steps) {
        mDurationBar.setProgress(steps);
    }

    @Override
    public void updateDurationBarMax(int steps) {
        mDurationBar.setMax(steps);
    }
}
