package com.skepticalone.mecachecker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Shift {

    private static final int MILLIS_PER_SECOND = 1000;
    private static final DateFormat sFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
    final Calendar start = new GregorianCalendar();
    final Calendar end = new GregorianCalendar();

    public Shift(long startSeconds, long endSeconds) {
        start.setTimeInMillis(startSeconds * MILLIS_PER_SECOND);
        end.setTimeInMillis(endSeconds * MILLIS_PER_SECOND);
    }

    public final Date getStart() {
        return start.getTime();
    }

    public final Date getEnd() {
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

    @Override
    public String toString() {
        return sFormat.format(start.getTime()) + " - " + sFormat.format(end.getTime());
    }

    final long timeSince(Shift lastShift) {
        return start.getTimeInMillis() - lastShift.end.getTimeInMillis();
    }

    public long getDuration() {
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

    public final long getContentValue(boolean isStart) {
        return (isStart ? start : end).getTimeInMillis() / 1000;
    }

    protected final boolean isSameDay() {
        return start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    final void advance(int field, int amount) {
        start.add(field, amount);
        end.add(field, amount);
    }

}
