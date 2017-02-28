package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class ComplianceCursor extends CursorWrapper {
    public final static int
            SHIFT_TYPE_NORMAL_DAY = 0,
            SHIFT_TYPE_LONG_DAY = 1,
            SHIFT_TYPE_NIGHT_SHIFT = 2;
    final static String[] PROJECTION = new String[]{
            ShiftContract.Shift._ID,
            ShiftContract.Shift.COLUMN_NAME_START,
            ShiftContract.Shift.COLUMN_NAME_END
    };
    private final static String[]
            COLUMN_NAMES,
            EXTRA_COLUMN_NAMES = new String[]{
                    "SHIFT_TYPE",
                    "TIME_BETWEEN_SHIFTS",
            "DURATION_OVER_DAY",
            "DURATION_OVER_WEEK",
            "DURATION_OVER_FORTNIGHT",
            "CURRENT_WEEKEND_START",
            "CURRENT_WEEKEND_END",
            "PREVIOUS_WEEKEND_WORKED_START",
            "PREVIOUS_WEEKEND_WORKED_END",
            "CONSECUTIVE_WEEKENDS_WORKED"
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_SHIFT_TYPE = 3,
            COLUMN_INDEX_TIME_BETWEEN_SHIFTS = 4,
            COLUMN_INDEX_DURATION_OVER_DAY = 5,
            COLUMN_INDEX_DURATION_OVER_WEEK = 6,
            COLUMN_INDEX_DURATION_OVER_FORTNIGHT = 7,
            COLUMN_INDEX_CURRENT_WEEKEND_START = 8,
            COLUMN_INDEX_CURRENT_WEEKEND_END = 9,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START = 10,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END = 11,
            COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED = 12;
    private final static int
            SHIFT_TYPE_OTHER = 3;
    static {
        COLUMN_NAMES = new String[PROJECTION.length + EXTRA_COLUMN_NAMES.length];
        System.arraycopy(PROJECTION, 0, COLUMN_NAMES, 0, PROJECTION.length);
        System.arraycopy(EXTRA_COLUMN_NAMES, 0, COLUMN_NAMES, PROJECTION.length, EXTRA_COLUMN_NAMES.length);
    }

    public ComplianceCursor(Cursor cursor) {
        super(cursor);
    }

    public long getId() {
        return getLong(COLUMN_INDEX_ID);
    }

    @NonNull
    public Interval getShift() {
        return new Interval(getLong(COLUMN_INDEX_START), getLong(COLUMN_INDEX_END));
    }

    public int getShiftType() {
        return getInt(COLUMN_INDEX_SHIFT_TYPE);
    }

    @Nullable
    public Duration getTimeBetweenShifts() {
        return isNull(COLUMN_INDEX_TIME_BETWEEN_SHIFTS) ? null : new Duration(getLong(COLUMN_INDEX_TIME_BETWEEN_SHIFTS));
    }

    @NonNull
    public Duration getDurationOverDay() {
        return new Duration(getLong(COLUMN_INDEX_DURATION_OVER_DAY));
    }

    @NonNull
    public Duration getDurationOverWeek() {
        return new Duration(getLong(COLUMN_INDEX_DURATION_OVER_WEEK));
    }

    @NonNull
    public Duration getDurationOverFortnight() {
        return new Duration(getLong(COLUMN_INDEX_DURATION_OVER_FORTNIGHT));
    }

    @Nullable
    public Interval getCurrentWeekend() {
        if (isNull(COLUMN_INDEX_CURRENT_WEEKEND_START) || isNull(COLUMN_INDEX_CURRENT_WEEKEND_END)) {
            return null;
        }
        return new Interval(getLong(COLUMN_INDEX_CURRENT_WEEKEND_START), getLong(COLUMN_INDEX_CURRENT_WEEKEND_END));
    }

    @Nullable
    public Interval getPreviousWeekend() {
        if (isNull(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START) || isNull(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END)) {
            return null;
        }
        return new Interval(getLong(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START), getLong(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END));
    }

    public boolean consecutiveWeekendsWorked() {
        return getInt(COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED) == 1;
    }

    static class ComplianceMatrixCursor extends MatrixCursor {

        ComplianceMatrixCursor(
                @NonNull Cursor initialCursor,
                @Nullable Long shiftId,
                int normalDayStartTotalMinutes,
                int normalDayEndTotalMinutes,
                int longDayStartTotalMinutes,
                int longDayEndTotalMinutes,
                int nightShiftStartTotalMinutes,
                int nightShiftEndTotalMinutes
        ) {
            super(COLUMN_NAMES, shiftId == null ? initialCursor.getCount() : 1);
            for (int i = 0, size = initialCursor.getCount(); i < size; i++) {
                initialCursor.moveToPosition(i);
                long id = initialCursor.getLong(COLUMN_INDEX_ID);
                if (shiftId != null && shiftId != id) continue;
                Interval currentShift = new Interval(initialCursor.getLong(COLUMN_INDEX_START), initialCursor.getLong(COLUMN_INDEX_END));
                MatrixCursor.RowBuilder builder = newRow()
                        .add(id)
                        .add(currentShift.getStartMillis())
                        .add(currentShift.getEndMillis());
                int startTotalMinutes = currentShift.getStart().getMinuteOfDay();
                int endTotalMinutes = currentShift.getEnd().getMinuteOfDay();
                if (startTotalMinutes == normalDayStartTotalMinutes && endTotalMinutes == normalDayEndTotalMinutes) {
                    builder.add(SHIFT_TYPE_NORMAL_DAY);
                } else if (startTotalMinutes == longDayStartTotalMinutes && endTotalMinutes == longDayEndTotalMinutes) {
                    builder.add(SHIFT_TYPE_LONG_DAY);
                } else if (startTotalMinutes == nightShiftStartTotalMinutes && endTotalMinutes == nightShiftEndTotalMinutes) {
                    builder.add(SHIFT_TYPE_NIGHT_SHIFT);
                } else {
                    builder.add(SHIFT_TYPE_OTHER);
                }
                if (initialCursor.moveToPrevious()) {
                    builder.add(currentShift.getStartMillis() - initialCursor.getLong(COLUMN_INDEX_END));
                } else {
                    builder.add(null);
                }
                builder
                        .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusDays(1).toInstant()).getMillis())
                        .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(1).toInstant()).getMillis())
                        .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(2).toInstant()).getMillis());
                Interval currentWeekend = getWeekend(currentShift);
                if (currentWeekend != null) {
                    builder
                            .add(currentWeekend.getStartMillis())
                            .add(currentWeekend.getEndMillis());
                    initialCursor.moveToPosition(i);
                    while (initialCursor.moveToPrevious()) {
                        Interval weekend = getWeekend(new Interval(initialCursor.getLong(COLUMN_INDEX_START), initialCursor.getLong(COLUMN_INDEX_END)));
                        if (weekend != null && !currentWeekend.equals(weekend)) {
                            builder
                                    .add(weekend.getStartMillis())
                                    .add(weekend.getEndMillis())
                                    .add(weekend.getStart().isEqual(currentWeekend.getStart().minusWeeks(1)) && weekend.getEnd().isEqual(currentWeekend.getEnd().minusWeeks(1)) ? 1 : 0);
                            break;
                        }
                    }
                }
                if (shiftId != null) break;
            }
            initialCursor.close();
        }

        @NonNull
        private static Duration getDurationSince(Cursor cursor, int positionToCheck, Instant cutOff) {
            Duration totalDuration = Duration.ZERO;
            cursor.moveToPosition(positionToCheck);
            do {
                Instant end = new Instant(cursor.getLong(COLUMN_INDEX_END));
                if (!end.isAfter(cutOff)) break;
                Instant start = new Instant(cursor.getLong(COLUMN_INDEX_START));
                totalDuration = totalDuration.plus(new Duration(cutOff.isAfter(start) ? cutOff : start, end));
            } while (cursor.moveToPrevious());
            return totalDuration;
        }

        @Nullable
        private static Interval getWeekend(Interval shift) {
            DateTime weekendStart = shift.getStart().withDayOfWeek(DateTimeConstants.SATURDAY).withTimeAtStartOfDay();
            Interval weekend = new Interval(weekendStart, weekendStart.plusDays(2));
            return shift.overlaps(weekend) ? weekend : null;
        }
    }
}
