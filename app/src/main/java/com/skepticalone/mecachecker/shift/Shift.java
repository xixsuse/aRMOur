package com.skepticalone.mecachecker.shift;

import com.skepticalone.mecachecker.BuildConfig;

import java.util.Calendar;
import java.util.GregorianCalendar;

class Shift {
    static final int MINUTES_PER_STEP = 5;
    private static final int MILLIS_PER_MINUTE = 1000 * 60;
    private static final int MILLIS_PER_STEP = MILLIS_PER_MINUTE * MINUTES_PER_STEP;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int STEPS_PER_HOUR = MINUTES_PER_HOUR / MINUTES_PER_STEP;
    private final Calendar mStart, mEnd, mMaxEnd;
    private final ShiftDisplayListener mListener;
    private long mMaxDurationInMillis;

    Shift(ShiftDisplayListener listener, Calendar start, Calendar end) {
        this(listener, start, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE));
    }

    Shift(ShiftDisplayListener listener, int year, int month, int dayOfMonth, int startHourOfDay, int startMinute, int endHourOfDay, int endMinute) {
        this(listener, new GregorianCalendar(year, month, dayOfMonth, startHourOfDay, startMinute), endHourOfDay, endMinute);
    }

    private Shift(ShiftDisplayListener listener, Calendar start, int endHourOfDay, int endMinute) {
        mListener = listener;
        mStart = start;
        mListener.updateDate(mStart);
        mListener.updateStart(mStart);
        mMaxEnd = new GregorianCalendar();
        updateMaxEnd();
        mEnd = new GregorianCalendar();
        updateEnd(endHourOfDay, endMinute);
    }

    @Override
    public String toString() {
        return mStart.toString() + " to " + mEnd.toString();
    }

    int getYear() {
        return mStart.get(Calendar.YEAR);
    }

    int getMonth() {
        return mStart.get(Calendar.MONTH);
    }

    int getDayOfMonth() {
        return mStart.get(Calendar.DAY_OF_MONTH);
    }

    int getHour(boolean isStart) {
        return (isStart ? mStart : mEnd).get(Calendar.HOUR_OF_DAY);
    }

    int getMinute(boolean isStart) {
        return (isStart ? mStart : mEnd).get(Calendar.MINUTE);
    }

    long getContentValue(boolean isStart) {
        return (isStart ? mStart : mEnd).getTimeInMillis() / 1000;
    }

    void updateDate(int year, int month, int dayOfMonth) {
        mStart.set(Calendar.YEAR, year);
        mStart.set(Calendar.MONTH, month);
        mStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mListener.updateDate(mStart);
        updateMaxEnd();
        updateEnd(getHour(false), getMinute(false));
    }

    void updateStart(int hourOfDay, int minute) {
        mStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mStart.set(Calendar.MINUTE, minute);
        mListener.updateStart(mStart);
        updateMaxEnd();
        updateEnd(getHour(false), getMinute(false));
    }

    private void updateMaxEnd() {
        mMaxEnd.setTime(mStart.getTime());
        mMaxEnd.add(Calendar.DATE, 1);
        long maxDurationInMillis = mMaxEnd.getTimeInMillis() - mStart.getTimeInMillis();
        if (mMaxDurationInMillis != maxDurationInMillis) {
            mMaxDurationInMillis = maxDurationInMillis;
            mListener.updateDurationBarMax((int) (mMaxDurationInMillis / MILLIS_PER_STEP));
        }
    }

    void updateEnd(int hourOfDay, int minute) {
        mEnd.setTime(mStart.getTime());
        mEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mEnd.set(Calendar.MINUTE, minute);
        while (!mEnd.after(mStart)) {
            mEnd.add(Calendar.DATE, 1);
        }
        if (BuildConfig.DEBUG && mEnd.after(mMaxEnd)) {
            throw new AssertionError();
        }
        mListener.updateEnd(mEnd, isSameDay());
        int steps = (int) ((mEnd.getTimeInMillis() - mStart.getTimeInMillis()) / MILLIS_PER_STEP);
        updateDurationText(steps);
        mListener.updateDurationBarProgress(steps);
    }

    private boolean isSameDay() {
        return mStart.get(Calendar.DAY_OF_MONTH) == mEnd.get(Calendar.DAY_OF_MONTH);
    }

    void onDurationUpdatedByUser(int steps) {
        mEnd.setTime(mStart.getTime());
        mEnd.add(Calendar.MINUTE, steps * MINUTES_PER_STEP);
        mListener.updateEnd(mEnd, isSameDay());
        updateDurationText(steps);
    }

    private void updateDurationText(int steps) {
        int hours = steps / STEPS_PER_HOUR;
        int minutes = steps % STEPS_PER_HOUR * MINUTES_PER_STEP;
        mListener.updateDuration(hours, minutes);
    }

    interface ShiftDisplayListener {
        void updateDate(Calendar date);

        void updateStart(Calendar start);

        void updateEnd(Calendar end, boolean sameDay);

        void updateDuration(int hours, int minutes);

        void updateDurationBarProgress(int steps);

        void updateDurationBarMax(int steps);
    }
}