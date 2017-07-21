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

}
