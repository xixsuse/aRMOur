package com.skepticalone.mecachecker;

import java.util.Calendar;


public class Shift {

    private final Calendar mStart;
    private final Calendar mEnd;

    public Shift(Calendar start, Calendar end) {
        this.mStart = start;
        this.mEnd = end;
    }

    @Override
    public String toString() {
        return mStart.toString() + " to " + mEnd.toString();
    }

    public long getStart() {
        return mStart.getTimeInMillis();
    }

    public long getEnd() {
        return mEnd.getTimeInMillis();
    }
}