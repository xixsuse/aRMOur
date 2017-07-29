package com.skepticalone.mecachecker.data.util;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class ShiftData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_START)
    private final DateTime start;

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_END)
    private final DateTime end;

    @NonNull
    @Ignore
    private final Duration duration;

    public ShiftData(
            @NonNull DateTime start,
            @NonNull DateTime end
    ) {
        this.start = start;
        this.end = end;
        duration = new Duration(start, end);
    }

    @NonNull
    public DateTime getStart() {
        return start;
    }

    @NonNull
    public DateTime getEnd() {
        return end;
    }

    @NonNull
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ShiftData) {
            ShiftData other = (ShiftData) object;
            return start.getMillis() == other.start.getMillis() && end.getMillis() == other.end.getMillis();
        }
        return false;
    }

    @Nullable
    public LocalDate getWeekend() {
        DateTime weekendStart = start.withDayOfWeek(DateTimeConstants.SATURDAY).withTimeAtStartOfDay();
        if (weekendStart.isBefore(end) && start.isBefore(weekendStart.plusDays(2))) {
            return weekendStart.toLocalDate();
        } else return null;
    }

    @NonNull
    private static DateTime getNewStart(@NonNull final LocalTime start, @Nullable final DateTime earliestStart) {
        DateTime newStart = (earliestStart == null ? DateTime.now() : earliestStart).withTime(start);
        if (earliestStart != null) {
            while (newStart.isBefore(earliestStart)) {
                newStart = newStart.plusDays(1);
            }
        }
        return newStart;
    }

    @NonNull
    public static DateTime getNewEnd(@NonNull final DateTime start, @NonNull final LocalTime end) {
        DateTime newEnd = start.withTime(end);
        while (!newEnd.isAfter(start)) {
            newEnd = newEnd.plusDays(1);
        }
        return newEnd;
    }

    @NonNull
    private DateTime getNewEnd(@NonNull final DateTime newStart) {
        return getNewEnd(newStart, end.toLocalTime());
    }

    @NonNull
    public ShiftData withNewDate(@NonNull final LocalDate newDate) {
        final DateTime newStart = start.withDate(newDate), newEnd = getNewEnd(newStart);
        return new ShiftData(newStart, newEnd);
    }

    @NonNull
    public ShiftData withNewTime(@NonNull final LocalTime time, boolean isStart) {
        if (isStart) {
            final DateTime newStart = start.withTime(time);
            return new ShiftData(newStart, getNewEnd(newStart, end.toLocalTime()));
        } else {
            return new ShiftData(start, getNewEnd(start, time));
        }
    }

    @NonNull
    public static ShiftData withEarliestStart(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final DateTime earliestStart) {
        final DateTime newStart = getNewStart(startTime, earliestStart),
                newEnd = getNewEnd(newStart, endTime);
        return new ShiftData(newStart, newEnd);
    }

}
