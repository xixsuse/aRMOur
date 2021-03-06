package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.TemporalAdjusters;

public final class ShiftData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_START)
    private final Instant start;

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_END)
    private final Instant end;

    public ShiftData(
            @NonNull Instant start,
            @NonNull Instant end
    ) {
        this.start = start;
        this.end = end;
    }

    @NonNull
    static ShiftData from(@NonNull final ZonedDateTime start, @NonNull final LocalTime endTime) {
        ZonedDateTime newEnd = start.with(endTime);
        while (!newEnd.isAfter(start)) {
            newEnd = newEnd.plusDays(1);
        }
        return new ShiftData(start.toInstant(), newEnd.toInstant());
    }

    @NonNull
    static ShiftData withEarliestStart(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final Instant earliestStart, @NonNull ZoneId zoneId, boolean skipWeekends) {
        ZonedDateTime newStart;
        if (earliestStart == null) {
            newStart = ZonedDateTime.now(zoneId).with(startTime);
        } else {
            newStart = earliestStart.atZone(zoneId).with(startTime);
            while (newStart.toInstant().isBefore(earliestStart)) {
                newStart = newStart.plusDays(1);
            }
        }
        if (skipWeekends) {
            DayOfWeek dayOfWeek = newStart.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                newStart = newStart.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
        }
        return from(newStart, endTime);
    }

    @NonNull
    static ShiftData withEarliestStartAfterMinimumDurationBetweenShifts(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final Instant earliestStart, @NonNull ZoneId zoneId, boolean skipWeekends) {
        return withEarliestStart(startTime, endTime, earliestStart == null ? null : earliestStart.plus(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)), zoneId, skipWeekends);
    }

    @NonNull
    public Instant getStart() {
        return start;
    }

    @NonNull
    public Instant getEnd() {
        return end;
    }

}
