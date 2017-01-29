package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class Shift {

    private static final int MILLIS_PER_SECOND = 1000;
    private final Calendar start = new GregorianCalendar();
    private final Calendar end = new GregorianCalendar();
    private final Calendar maxEnd = new GregorianCalendar();
    private long maxDuration, currentDuration;

    protected Shift(long startSeconds, long endSeconds) {
        start.setTimeInMillis(startSeconds * MILLIS_PER_SECOND);
        end.setTimeInMillis(endSeconds * MILLIS_PER_SECOND);
        updateMaxEnd();
        checkEndAndSetDuration();
    }

    protected final Date getStart() {
        return start.getTime();
    }

    protected final Date getEnd() {
        return end.getTime();
    }

    public final int getYear() {
        return start.get(Calendar.YEAR);
    }

    public final int getMonth() {
        return start.get(Calendar.MONTH);
    }

    public final int getDayOfMonth() {
        return start.get(Calendar.DAY_OF_MONTH);
    }

    public final int getHour(boolean isStart) {
        return (isStart ? start : end).get(Calendar.HOUR_OF_DAY);
    }

    public final int getMinute(boolean isStart) {
        return (isStart ? start : end).get(Calendar.MINUTE);
    }

    protected final long getCurrentDuration() {
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

    protected final void updateStart(int hourOfDay, int minute) {
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

    public final long getContentValue(boolean isStart) {
        return (isStart ? start : end).getTimeInMillis() / 1000;
    }

    protected final boolean isSameDay() {
        return start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }

    private void updateMaxEnd() {
        maxEnd.setTime(start.getTime());
        maxEnd.add(Calendar.DATE, 1);
        maxDuration = maxEnd.getTimeInMillis() - start.getTimeInMillis();
    }

}