package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CheckedShift extends Period {

    private final Calendar maxEnd = new GregorianCalendar();
    private long maxDuration, currentDuration;

    protected CheckedShift(long startSeconds, long endSeconds) {
        super(startSeconds, endSeconds);
        updateMaxEnd();
        checkEndAndSetDuration();
    }

    @Override
    public final long getDuration() {
        return currentDuration;
    }

    protected final long getMaxDuration() {
        return maxDuration;
    }

    protected final void updateDate(int year, int month, int dayOfMonth) {
        start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        start.set(Calendar.MONTH, month);
        start.set(Calendar.YEAR, year);
        updateMaxEnd();
        updateEnd();
    }

    protected void updateStart(int hourOfDay, int minute) {
        start.set(Calendar.HOUR_OF_DAY, hourOfDay);
        start.set(Calendar.MINUTE, minute);
        updateMaxEnd();
        updateEnd();
    }

    private void updateEnd() {
        updateEnd(end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE));
    }

    protected final void updateEnd(int hourOfDay, int minute) {
        end.setTime(start.getTime());
        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
        end.set(Calendar.MINUTE, minute);
        while (!end.after(start)) {
            end.add(Calendar.DATE, 1);
        }
        checkEndAndSetDuration();
    }

    private void checkEndAndSetDuration() {
        currentDuration = end.getTimeInMillis() - start.getTimeInMillis();
        if (BuildConfig.DEBUG && currentDuration > maxDuration) {
            throw new AssertionError();
        }
    }

    protected final void updateEnd(long newDuration) {
        if (newDuration <= maxDuration) {
            currentDuration = newDuration;
            end.setTimeInMillis(start.getTimeInMillis() + currentDuration);
        }
    }


    private void updateMaxEnd() {
        maxEnd.setTime(start.getTime());
        maxEnd.add(Calendar.DATE, 1);
        maxDuration = maxEnd.getTimeInMillis() - start.getTimeInMillis();
    }


}