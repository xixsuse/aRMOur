package com.skepticalone.mecachecker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShiftActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerFragment.OnShiftTimeSetListener {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final int DEFAULT_START_HOUR = 8;
    private static final int DEFAULT_START_MINUTE = 5;
    private static final int DEFAULT_END_HOUR = 15;
    private static final int DEFAULT_END_MINUTE = 40;
    private static final long MILLIS_IN_HOUR = 1000 * 60 * 60;
    private TextView mDateView, mStartTimeView, mEndTimeView, mDurationView;
    private double mDurationInHours;
    private final Calendar mStart = new GregorianCalendar(), mEnd = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
        mDateView = (TextView) findViewById(R.id.date);
        assert mDateView != null;
        mStartTimeView = (TextView) findViewById(R.id.startTime);
        assert mStartTimeView != null;
        mEndTimeView = (TextView) findViewById(R.id.endTime);
        assert mEndTimeView != null;
        mDurationView = (TextView) findViewById(R.id.duration);
        assert mDurationView != null;
        mStart.set(Calendar.MILLISECOND, 0);
        mStart.set(Calendar.SECOND, 0);
        setStart(DEFAULT_START_HOUR, DEFAULT_START_MINUTE);
        setEnd(DEFAULT_END_HOUR, DEFAULT_END_MINUTE);
        updateTextViews();
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
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        setStart(hourOfDay, minute);
        updateTextViews();
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        setEnd(hourOfDay, minute);
        updateTextViews();
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
        mEnd.setTime(mStart.getTime());
        mEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mEnd.set(Calendar.MINUTE, minute);
        while (!mEnd.after(mStart)) {
            mEnd.add(Calendar.DATE, 1);
        }
        mDurationInHours = ((double) (mEnd.getTimeInMillis() - mStart.getTimeInMillis())) / MILLIS_IN_HOUR;
    }

    private void updateTextViews() {
        updateDate();
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    private void updateDate() {
        mDateView.setText(getString(R.string.date_format, mStart));
    }

    private void updateStartTime() {
        mStartTimeView.setText(getString(R.string.time_format, mStart));
    }

    private void updateEndTime() {
        String timeString = getString(R.string.time_format, mEnd);
        if (mEnd.get(Calendar.DATE) != mStart.get(Calendar.DATE)) {
            timeString = getString(R.string.plus_1_day, timeString);
        }
        mEndTimeView.setText(timeString);
    }

    private void updateDuration() {
        mDurationView.setText(getString(R.string.duration_format, mDurationInHours));
    }

}
