package com.skepticalone.mecachecker.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
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
    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
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
    private static final PeriodFormatter weeksAgoFormatter = new PeriodFormatterBuilder()
            .appendWeeks()
            .appendSuffix(" week ago", " weeks ago")
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

    private static String getQualifiedString(@NonNull String main, @NonNull String qualifier) {
        return main + " (" + qualifier + ")";
    }

    private static LocalTime getTime(int totalMinutes) {
        return new LocalTime(calculateHours(totalMinutes), calculateMinutes(totalMinutes));
    }

    public static String getTimeString(int totalMinutes) {
        return timeFormatter.print(getTime(totalMinutes));
    }

    public static String getStartTimeString(@NonNull LocalTime time) {
        return timeFormatter.print(time);
    }

    public static String getEndTimeString(@NonNull DateTime time, @NonNull LocalDate startDate) {
        String timeString = timeFormatter.print(time.toLocalTime());
        if (!time.toLocalDate().isEqual(startDate)) {
            timeString = getQualifiedString(timeString, dayFormatter.print(time));
        }
        return timeString;
    }

    public static String getDateTimeString(@NonNull DateTime dateTime) {
        return dateTimeFormatter.print(dateTime);
    }

    public static String getFullDateString(@NonNull LocalDate date) {
        return fullDateFormatter.print(date);
    }

    private static String getSpanString(@NonNull String start, @NonNull String end) {
        return start + " - " + end;
    }

    public static String getWeekendDateSpanString(@NonNull LocalDate saturday) {
        return getSpanString(dateFormatter.print(saturday), dateFormatter.print(saturday.plusDays(1)));
    }

    public static String getTimeSpanString(@NonNull ShiftData shift) {
        return getSpanString(timeFormatter.print(shift.getStart()), getEndTimeString(shift.getEnd(), shift.getStart().toLocalDate()));
    }

    public static String getTimeSpanString(@NonNull SharedPreferences sharedPreferences, @NonNull String startKey, @NonNull String endKey) {
        int startTotalMinutes = sharedPreferences.getInt(startKey, 0),
                endTotalMinutes = sharedPreferences.getInt(endKey, 0);
        return getSpanString(
                timeFormatter.print(new LocalTime(calculateHours(startTotalMinutes), calculateMinutes(startTotalMinutes))),
                timeFormatter.print(new LocalTime(calculateHours(endTotalMinutes), calculateMinutes(endTotalMinutes)))
        );
    }

    public static String getWeeksAgo(@NonNull LocalDate lastWeekendWorked, @NonNull LocalDate currentWeekend) {
        return weeksAgoFormatter.print(new Period(lastWeekendWorked, currentWeekend, PeriodType.weeks()));
    }

    public static String getPeriodString(@NonNull Period period) {
        return periodFormatter.print(period);
    }

    public static String getPeriodString(@NonNull Duration duration) {
        return getPeriodString(duration.toPeriod());
    }

}
