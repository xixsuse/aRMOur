package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
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

public abstract class RawShift extends Item {

    @NonNull
    @Embedded
    private final ShiftData shiftData;

    RawShift(
            long id,
            @Nullable String comment,
            @NonNull ShiftData shiftData
    ) {
        super(id, comment);
        this.shiftData = shiftData;
    }

    @NonNull
    public final ShiftData getShiftData() {
        return shiftData;
    }

    public static final class ShiftData {

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
        static ShiftData from(@NonNull final ZonedDateTime start, @NonNull final LocalTime endTime){
            ZonedDateTime newEnd = start.with(endTime);
            while (!newEnd.isAfter(start)) {
                newEnd = newEnd.plusDays(1);
            }
            return new ShiftData(start.toInstant(), newEnd.toInstant());
        }

        @NonNull
        static ShiftData withEarliestStart(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final Instant earliestStart, @NonNull ZoneId zoneId, boolean skipWeekends) {
            ZonedDateTime newStart = (earliestStart == null ? Instant.now() : earliestStart).atZone(zoneId).with(startTime);
            while ((earliestStart != null && newStart.toInstant().isBefore(earliestStart)) || (skipWeekends && (newStart.getDayOfWeek() == DayOfWeek.SATURDAY || newStart.getDayOfWeek() == DayOfWeek.SUNDAY))) {
                newStart = newStart.plusDays(1);
            }
            return from(newStart, endTime);
        }

        @NonNull
        static ShiftData withEarliestStartAfterMinimumDurationBetweenShifts(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final Instant earliestStart, @NonNull ZoneId zoneId, boolean skipWeekends) {
            return withEarliestStart(startTime, endTime, earliestStart == null ? null : earliestStart.plus(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)), zoneId, skipWeekends);
        }
//
//        @NonNull
//        static ShiftData withNewDate(@NonNull Shift.ShiftData shiftData, @NonNull LocalDate newDate) {
//            return from(shiftData.getStart().with(newDate), shiftData.getEnd().toLocalTime());
//        }

        @NonNull
        public Instant getStart() {
            return start;
        }

        @NonNull
        public Instant getEnd() {
            return end;
        }
//
        //
    //    @NonNull
    //    public Duration getDuration() {
    //        return Duration.between(start, end);
    //    }
//
//        @NonNull
//        public ShiftData withNewDate(@NonNull final LocalDate newDate, @NonNull ZoneId zoneId) {
//            final ZonedDateTime newStart = getStart().atZone(zoneId).with(newDate), newEnd = getNewEnd(newStart, getEnd().atZone(zoneId).toLocalTime());
//            return new ShiftData(newStart.toInstant(), newEnd.toInstant());
//        }
//
//        @NonNull
//        public ShiftData withNewTime(@NonNull final LocalTime time, @NonNull ZoneId zoneId, boolean isStart) {
//            final ZonedDateTime oldStart = getStart().atZone(zoneId);
//            final LocalTime oldEndTime = getEnd().atZone(zoneId).toLocalTime();
//            if (isStart) {
//                final ZonedDateTime newStart = oldStart.with(time);
//                return new ShiftData(newStart.toInstant(), getNewEnd(newStart, oldEndTime).toInstant());
//            } else {
//                return new ShiftData(getStart(), getNewEnd(oldStart, oldEndTime).toInstant());
//            }
//        }
//
//        @Override
//        public String toString() {
//            return getStart().toString() + " - " + getEnd().toString();
//        }

    }
}
