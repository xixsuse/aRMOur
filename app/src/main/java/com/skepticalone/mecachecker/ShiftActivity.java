package com.skepticalone.mecachecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ShiftActivity extends AppCompatActivity {

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
        setTime(true, 8, 0);
        setTime(false, 16, 15);
        updateDate();
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    private void setTime(boolean start, int hour, int minute) {
        if (start){
            mStart.set(Calendar.HOUR_OF_DAY, hour);
            mStart.set(Calendar.MINUTE, minute);
        }
        int endHour = start ? mEnd.get(Calendar.HOUR_OF_DAY) : hour;
        int endMinute = start ? mEnd.get(Calendar.MINUTE) : minute;
        mEnd.setTime(mStart.getTime());
        mEnd.set(Calendar.MINUTE, endMinute);
        mEnd.set(Calendar.HOUR_OF_DAY, endHour);
        while (mEnd.before(mStart)) {
            mEnd.add(Calendar.DATE, 1);
        }
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
}
