package com.skepticalone.mecachecker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShiftActivity
        extends
        AppCompatActivity
        implements
        DatePickerDialog.OnDateSetListener,
        TimePickerFragment.OnShiftTimeSetListener,
        SeekBar.OnSeekBarChangeListener
{

    public static final String TAG = "ShiftActivity";
    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final int DEFAULT_START_HOUR = 8;
    private static final int DEFAULT_START_MINUTE = 5;
    private static final int DEFAULT_END_HOUR = 15;
    private static final int DEFAULT_END_MINUTE = 40;
    public static final int MINUTES_PER_STEP = 5;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int STEPS_PER_HOUR = MINUTES_PER_HOUR / MINUTES_PER_STEP;
    private static final int MILLIS_PER_MINUTE = 1000 * 60;
    private static final int MILLIS_PER_STEP = MILLIS_PER_MINUTE * MINUTES_PER_STEP;
    private TextView mDateView, mStartTimeView, mEndTimeView, mDurationView;
    private SeekBar mDurationBar;
    private long mDurationInMillis, mMaxDurationInMillis;
    private final Calendar mStart = new GregorianCalendar(), mEnd = new GregorianCalendar(), mMaxEnd = new GregorianCalendar();

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
        mStart.set(Calendar.SECOND, 0);
        setStart(DEFAULT_START_HOUR, DEFAULT_START_MINUTE);
        setEnd(DEFAULT_END_HOUR, DEFAULT_END_MINUTE);
        mDurationBar.setOnSeekBarChangeListener(this);
        updateTextViews();
        updateDurationBarMax();
        updateDurationBarProgress();
    }

    public void onDateClicked(@SuppressWarnings("UnusedParameters") View v) {
        DatePickerFragment.create(
                mStart.get(Calendar.YEAR),
                mStart.get(Calendar.MONTH),
                mStart.get(Calendar.DAY_OF_MONTH)
        ).show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    public void onStartTimeClicked(@SuppressWarnings("UnusedParameters") View v) {
        showTimePickerFragment(true);
    }

    public void onEndTimeClicked(@SuppressWarnings("UnusedParameters") View v) {
        showTimePickerFragment(false);
    }

    private void showTimePickerFragment(boolean isStart) {
        Calendar calendar = isStart ? mStart : mEnd;
        TimePickerFragment.create(
                isStart,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        ).show(getSupportFragmentManager(), TIME_PICKER_FRAGMENT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        setDate(year, month, dayOfMonth);
        updateTextViews();
        updateDurationBarMax();
        updateDurationBarProgress();
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        setStart(hourOfDay, minute);
        updateTextViews();
        updateDurationBarMax();
        updateDurationBarProgress();
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        setEnd(hourOfDay, minute);
        updateEndTime();
//        updateDuration();
        updateDurationBarProgress();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mEnd.setTime(mStart.getTime());
            mEnd.add(Calendar.MINUTE, progress * MINUTES_PER_STEP);
            mDurationInMillis = mEnd.getTimeInMillis() - mStart.getTimeInMillis();
            updateEndTime();
        } else {
            Log.i(TAG, "onProgressChanged programmatically: progress = " + progress);
        }
        int hours = progress / STEPS_PER_HOUR;
        int minutes = progress % STEPS_PER_HOUR * MINUTES_PER_STEP;
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
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mStart.set(Calendar.YEAR, year);
        mStart.set(Calendar.MONTH, month);
        mStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setEnd(mEnd.get(Calendar.HOUR_OF_DAY), mEnd.get(Calendar.MINUTE));
    }

    private void setStart(int hourOfDay, int minute) {
        mStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mStart.set(Calendar.MINUTE, minute);
        setEnd(mEnd.get(Calendar.HOUR_OF_DAY), mEnd.get(Calendar.MINUTE));
    }

    private void setEnd(int hourOfDay, int minute) {
        mMaxEnd.setTime(mStart.getTime());
        mMaxEnd.add(Calendar.DATE, 1);
        mMaxDurationInMillis = mMaxEnd.getTimeInMillis() - mStart.getTimeInMillis();
        mEnd.setTime(mStart.getTime());
        mEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mEnd.set(Calendar.MINUTE, minute);
        while (!mEnd.after(mStart)) {
            mEnd.add(Calendar.DATE, 1);
        }
        if (BuildConfig.DEBUG && mEnd.after(mMaxEnd)){
            throw new AssertionError();
        }
        mDurationInMillis = mEnd.getTimeInMillis() - mStart.getTimeInMillis();
    }

    private void updateTextViews() {
        updateDate();
        updateStartTime();
        updateEndTime();
    }

    private void updateDate() {
        mDateView.setText(getString(R.string.date_format, mStart));
    }

    private void updateStartTime() {
        mStartTimeView.setText(getString(R.string.time_format, mStart));
    }

    private void updateEndTime() {
        mEndTimeView.setText(getString(mEnd.get(Calendar.DATE) == mStart.get(Calendar.DATE) ? R.string.time_format : R.string.time_format_with_day, mEnd));
    }

    private void updateDurationBarMax() {
        mDurationBar.setMax((int) (mMaxDurationInMillis / MILLIS_PER_STEP));
    }

    private void updateDurationBarProgress() {
        mDurationBar.setProgress((int) (mDurationInMillis / MILLIS_PER_STEP));
    }

}
