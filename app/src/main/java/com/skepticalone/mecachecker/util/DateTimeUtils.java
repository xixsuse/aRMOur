package com.skepticalone.mecachecker.util;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import static org.joda.time.DateTimeConstants.MINUTES_PER_HOUR;

public final class DateTimeUtils {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortDateTime();
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
    private static final DateTimeFormatter fullDateFormatter = DateTimeFormat.fullDate();
    private static final DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
    private static final DateTimeFormatter dayFormatter = new DateTimeFormatterBuilder().appendDayOfWeekShortText().toFormatter();
    private final static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendYears()
            .appendSuffix(" year", " years")
            .appendSeparator(", ")
            .appendMonths()
            .appendSuffix(" month", " months")
            .appendSeparator(", ")
            .appendWeeks()
            .appendSuffix(" week", " weeks")
            .appendSeparator(", ")
            .appendDays()
            .appendSuffix(" day", " days")
            .appendSeparator(", ")
            .appendHours()
            .appendSuffix(" hour", " hours")
            .appendSeparator(", ")
            .appendMinutes()
            .appendSuffix(" minute", " minutes")
            .toFormatter();

    public static int calculateTotalMinutes(int hours, int minutes) {
        return hours * MINUTES_PER_HOUR + minutes;
    }

    public static int calculateHours(int totalMinutes) {
        return totalMinutes / MINUTES_PER_HOUR;
    }

    public static int calculateMinutes(int totalMinutes) {
        return totalMinutes % MINUTES_PER_HOUR;
    }

    private static String getQualifiedString(String main, String qualifier) {
        return main + " (" + qualifier + ")";
    }

    private static LocalTime getTime(int totalMinutes) {
        return new LocalTime(calculateHours(totalMinutes), calculateMinutes(totalMinutes));
    }

    public static String getTimeString(int totalMinutes) {
        return timeFormatter.print(getTime(totalMinutes));
    }

    public static String getTimeString(Interval shift, boolean isStart, @Nullable DateTime startOfRosteredShift) {
        DateTime time = isStart ? shift.getStart() : shift.getEnd();
        if (
                isStart ?
                        (startOfRosteredShift == null || startOfRosteredShift.withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())) :
                        (startOfRosteredShift == null ? shift.getStart() : startOfRosteredShift).withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())
                ) {
            return timeFormatter.print(time);
        } else {
            return getQualifiedString(timeFormatter.print(time), dayFormatter.print(time));
        }
    }

    public static String getDateTimeString(DateTime dateTime) {
        return dateTimeFormatter.print(dateTime);
    }

    public static String getDateTimeString(long instant) {
        return dateTimeFormatter.print(instant);
    }

    public static String getFullDateString(DateTime date) {
        return fullDateFormatter.print(date);
    }

    private static String getSpanString(String start, String end) {
        return start + " - " + end;
    }

    public static String getDateSpanString(Interval shift) {
        return getSpanString(dateFormatter.print(shift.getStart()), dateFormatter.print(shift.getEnd()));
    }

    public static String getTimeSpanString(Interval shift) {
        return getSpanString(timeFormatter.print(shift.getStart()), timeFormatter.print(shift.getEnd()));
    }

    public static String getDoubleTimeSpanString(Interval rosteredShift, Interval loggedShift) {
        return getQualifiedString(getTimeSpanString(rosteredShift), getTimeSpanString(loggedShift));
    }

    public static String getTimeSpanString(SharedPreferences sharedPreferences, String startKey, String endKey) {
        int startTotalMinutes = sharedPreferences.getInt(startKey, 0),
                endTotalMinutes = sharedPreferences.getInt(endKey, 0);
        return getSpanString(
                timeFormatter.print(new LocalTime(calculateHours(startTotalMinutes), calculateMinutes(startTotalMinutes))),
                timeFormatter.print(new LocalTime(calculateHours(endTotalMinutes), calculateMinutes(endTotalMinutes)))
        );
    }

    public static String getShiftTypeWithDurationString(String shiftType, Period shift) {
        return getQualifiedString(shiftType, periodFormatter.print(shift));
    }

}
