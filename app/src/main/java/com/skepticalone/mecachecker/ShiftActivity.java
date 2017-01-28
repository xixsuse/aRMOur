package com.skepticalone.mecachecker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

public class ShiftActivity
        extends
        AppCompatActivity
        implements
        DatePickerDialog.OnDateSetListener,
        TimePickerFragment.OnShiftTimeSetListener,
        SeekBar.OnSeekBarChangeListener,
        Shift.ShiftDisplayListener
{

    //    public static final String TAG = "ShiftActivity";
    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";

    private TextView mDateView, mStartTimeView, mEndTimeView, mDurationView;
    private SeekBar mDurationBar;
    private Shift mShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
        mDateView = (TextView) findViewById(R.id.date);
        mStartTimeView = (TextView) findViewById(R.id.startTime);
        mEndTimeView = (TextView) findViewById(R.id.endTime);
        mDurationView = (TextView) findViewById(R.id.duration);
        mDurationBar = (SeekBar) findViewById(R.id.durationBar);
        if (BuildConfig.DEBUG && (mDateView == null || mStartTimeView == null || mEndTimeView == null || mDurationView == null || mDurationBar == null)) {
            throw new AssertionError();
        }
        mDurationBar.setOnSeekBarChangeListener(this);
        mShift = new Shift(this, 2016, 3, 24, 8, 10, 16, 35);
    }

    public void onDateClicked(@SuppressWarnings("UnusedParameters") View v) {
        DatePickerFragment.create(mShift).show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    public void onStartTimeClicked(@SuppressWarnings("UnusedParameters") View v) {
        showTimePickerFragment(true);
    }

    public void onEndTimeClicked(@SuppressWarnings("UnusedParameters") View v) {
        showTimePickerFragment(false);
    }

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
        TimePickerFragment.create(isStart, mShift).show(getSupportFragmentManager(), TIME_PICKER_FRAGMENT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
