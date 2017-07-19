package com.skepticalone.mecachecker.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
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

    public static String getStartTimeString(LocalTime time) {
        return timeFormatter.print(time);
    }

    public static String getEndTimeString(DateTime time, LocalDate startDate) {
        String timeString = timeFormatter.print(time.toLocalTime());
        if (!time.toLocalDate().isEqual(startDate)) {
            timeString = getQualifiedString(timeString, dayFormatter.print(time));
        }
        return timeString;
    }

//    public static String getStartTimeString(Interval shift, boolean isStart, @Nullable DateTime startOfRosteredShift) {
//        DateTime time = isStart ? shift.getStart() : shift.getEnd();
//        if (isStart) {
//            if (startOfRosteredShift == null) {
//                return timeFormatter.print(time);
//            } else {
//                return getStartTimeString(time, startOfRosteredShift)
//            }
//        }
//        if (
//                isStart ?
//                        (startOfRosteredShift == null || startOfRosteredShift.withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())) :
//                        (startOfRosteredShift == null ? shift.getStart() : startOfRosteredShift).withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())
//                ) {
//            return timeFormatter.print(time);
//        } else {
//            return getQualifiedString(timeFormatter.print(time), dayFormatter.print(time));
//        }
//
//        if (
//                isStart ?
//                        (startOfRosteredShift == null || startOfRosteredShift.withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())) :
//                        (startOfRosteredShift == null ? shift.getStart() : startOfRosteredShift).withTimeAtStartOfDay().isEqual(time.withTimeAtStartOfDay())
//                ) {
//            return timeFormatter.print(time);
//        } else {
//            return getQualifiedString(timeFormatter.print(time), dayFormatter.print(time));
//        }
//    }

    public static String getDateTimeString(DateTime dateTime) {
        return dateTimeFormatter.print(dateTime);
    }

    public static String getDateTimeString(long instant) {
        return dateTimeFormatter.print(instant);
    }

    public static String getFullDateString(LocalDate date) {
        return fullDateFormatter.print(date);
    }

    private static String getSpanString(String start, String end) {
        return start + " - " + end;
    }

    public static String getDateSpanString(LocalDate date1, LocalDate date2) {
        return getSpanString(dateFormatter.print(date1), dateFormatter.print(date2));
    }

    public static String getTimeSpanString(@NonNull ShiftData shift) {
        return getSpanString(timeFormatter.print(shift.getStart()), getEndTimeString(shift.getEnd(), shift.getStart().toLocalDate()));
    }

    public static String getTimeSpanString(@NonNull ShiftData rosteredShift, @Nullable ShiftData loggedShift) {
        String timeSpan = getTimeSpanString(rosteredShift);
        if (loggedShift != null) {
            timeSpan = getQualifiedString(timeSpan, getTimeSpanString(loggedShift));
        }
        return timeSpan;
    }

    public static String getTimeSpanString(SharedPreferences sharedPreferences, String startKey, String endKey) {
        int startTotalMinutes = sharedPreferences.getInt(startKey, 0),
                endTotalMinutes = sharedPreferences.getInt(endKey, 0);
        return getSpanString(
                timeFormatter.print(new LocalTime(calculateHours(startTotalMinutes), calculateMinutes(startTotalMinutes))),
                timeFormatter.print(new LocalTime(calculateHours(endTotalMinutes), calculateMinutes(endTotalMinutes)))
        );
    }

    public static String getPeriodString(Duration duration) {
        return periodFormatter.print(duration.toPeriod());
    }

    public static String getShiftTypeWithDurationString(String shiftType, Interval shift) {
        return getQualifiedString(shiftType, periodFormatter.print(shift.toPeriod()));
    }

}
