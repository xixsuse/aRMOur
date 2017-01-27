package com.skepticalone.mecachecker;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ShiftActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PICKER_FRAGMENT = "date_picker_fragment";
    private TextView mDateView, mStartTimeView, mEndTimeView, mDurationView;
    private final Calendar mStart = new GregorianCalendar(), mEnd = new GregorianCalendar();
    private final DateFormat mTimeFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.US);
    private final DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private final static long MILLIS_IN_HOUR = 1000 * 60 * 60;

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
        setStartTime(8, 0);
        setEndTime(16, 15);
        updateTextViews();
    }

//    private void setTime(boolean start, int hour, int minute) {
//        if (start){
//            mStart.set(Calendar.HOUR_OF_DAY, hour);
//            mStart.set(Calendar.MINUTE, minute);
//        }
//        int endHour = start ? mEnd.get(Calendar.HOUR_OF_DAY) : hour;
//        int endMinute = start ? mEnd.get(Calendar.MINUTE) : minute;
//        mEnd.setTime(mStart.getTime());
//        mEnd.set(Calendar.MINUTE, endMinute);
//        mEnd.set(Calendar.HOUR_OF_DAY, endHour);
//        while (mEnd.before(mStart)) {
//            mEnd.add(Calendar.DATE, 1);
//        }
//    }

    private void setStartTime(int hour, int minute){
        mStart.set(Calendar.HOUR_OF_DAY, hour);
        mStart.set(Calendar.MINUTE, minute);
        setEndTime(mEnd.get(Calendar.HOUR_OF_DAY), mEnd.get(Calendar.MINUTE));
    }

    private void setEndTime(int hour, int minute){
        mEnd.setTime(mStart.getTime());
        mEnd.set(Calendar.HOUR_OF_DAY, hour);
        mEnd.set(Calendar.MINUTE, minute);
        ensureEndAfterStart();
    }

    private void ensureEndAfterStart(){
        while (mEnd.before(mStart)) {
            mEnd.add(Calendar.DATE, 1);
        }
    }

    private void updateTextViews(){
        updateDate();
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    private void updateDate(){
        mDateView.setText("Date: " + mDateFormat.format(mStart.getTime()));
    }

    private void updateStartTime(){
        mStartTimeView.setText("Start: " + mTimeFormat.format(mStart.getTime()));
    }

    private void updateEndTime(){
        mEndTimeView.setText("End: " + mTimeFormat.format(mEnd.getTime()));
    }

    private void updateDuration() {
        double durationInMillis = mEnd.getTimeInMillis() - mStart.getTimeInMillis();
        mDurationView.setText(durationInMillis / MILLIS_IN_HOUR + " hours");
    }

    public void onDateClicked(View v){
        DialogFragment fragment = new DatePickerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(DatePickerFragment.YEAR, mStart.get(Calendar.YEAR));
        arguments.putInt(DatePickerFragment.MONTH, mStart.get(Calendar.MONTH));
        arguments.putInt(DatePickerFragment.DAY_OF_MONTH, mStart.get(Calendar.DAY_OF_MONTH));
        fragment.setArguments(arguments);
        fragment.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mStart.set(Calendar.YEAR, year);
        mStart.set(Calendar.MONTH, month);
        mStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setEndTime(mEnd.get(Calendar.HOUR_OF_DAY), mEnd.get(Calendar.MINUTE));
        updateTextViews();
    }
}
