package com.skepticalone.armour.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.FormatStyle;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoField;

import java.util.Locale;

public final class DateTimeUtils {

    private final static int MINUTES_PER_HOUR = 60;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    private static final DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static final DateTimeFormatter dayFormatter = new DateTimeFormatterBuilder().appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT).toFormatter();
//    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
//            .appendYears()
//            .appendSuffix(" year", " years")
//            .appendSeparator(", ")
//            .appendMonths()
//            .appendSuffix(" month", " months")
//            .appendSeparator(", ")
//            .appendWeeks()
//            .appendSuffix(" week", " weeks")
//            .appendSeparator(", ")
//            .appendDays()
//            .appendSuffix(" day", " days")
//            .appendSeparator(", ")
//            .appendHours()
//            .appendSuffix(" hour", " hours")
//            .appendSeparator(", ")
//            .appendMinutes()
//            .appendSuffix(" minute", " minutes")
//            .toFormatter();
//    private static final PeriodFormatter weeksAgoFormatter = new PeriodFormatterBuilder()
//            .appendWeeks()
//            .appendSuffix(" week ago", " weeks ago")
//            .toFormatter();

    public static int calculateTotalMinutes(int hours, int minutes) {
        return hours * MINUTES_PER_HOUR + minutes;
    }

    public static int calculateTotalMinutes(LocalTime time) {
        return calculateTotalMinutes(time.getHour(), time.getMinute());
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

    public static LocalTime getTime(int totalMinutes) {
        return LocalTime.of(calculateHours(totalMinutes), calculateMinutes(totalMinutes));
    }

    public static String getTimeString(int totalMinutes) {
        return timeFormatter.format(getTime(totalMinutes));
    }

    public static String getStartTimeString(@NonNull LocalTime time) {
        return timeFormatter.format(time);
    }

    public static String getEndTimeString(@NonNull LocalDateTime endDateTime, @NonNull LocalDate startDate) {
        String endTimeString = timeFormatter.format(endDateTime);
        if (!endDateTime.toLocalDate().isEqual(startDate)) {
            endTimeString = getQualifiedString(endTimeString, dayFormatter.format(endDateTime));
        }
        return endTimeString;
    }

    public static String getDateTimeString(@NonNull LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String getFullDateString(@NonNull LocalDate date) {
        return fullDateFormatter.format(date);
    }

    private static String getSpanString(@NonNull String start, @NonNull String end) {
        return start + " - " + end;
    }

    public static String getWeekendDateSpanString(@NonNull LocalDate saturday) {
        return getSpanString(dateFormatter.format(saturday), dateFormatter.format(saturday.plusDays(1)));
    }

    public static String getTimeSpanString(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        return getSpanString(timeFormatter.format(start), getEndTimeString(end, start.toLocalDate()));
    }

    public static String getTimeSpanString(@NonNull SharedPreferences sharedPreferences, @NonNull String startKey, @NonNull String endKey) {
        return getSpanString(
                timeFormatter.format(getTime(sharedPreferences.getInt(startKey, 0))),
                timeFormatter.format(getTime(sharedPreferences.getInt(endKey, 0)))
        );
    }

    public static String getWeeksAgo(@NonNull LocalDate lastWeekendWorked, @NonNull LocalDate currentWeekend) {
        return String.format(Locale.US, "Weeks ago: %d", (currentWeekend.toEpochDay() - lastWeekendWorked.toEpochDay()) / 7);
//        return weeksAgoFormatter.format(Period.between(lastWeekendWorked, currentWeekend).to());
    }

    public static String getPeriodString(@NonNull Period period) {
        return period.toString();
    }

    public static String getDurationString(@NonNull Duration duration) {
        return duration.toString();
    }

}
