package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

public final class Compliance {

    private static final String[] RAW_PROJECTION = new String[]{

            ShiftContract.RosteredShifts._ID,
            ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
            ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START,
            ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END,
    },
            EXTRA_COLUMN_NAMES = new String[]{
                    "LAST_SHIFT_ROSTERED_END",
                    "DURATION_OVER_DAY",
                    "DURATION_OVER_WEEK",
                    "DURATION_OVER_FORTNIGHT",
                    "CURRENT_WEEKEND_START",
                    "CURRENT_WEEKEND_END",
                    "PREVIOUS_WEEKEND_WORKED_START",
                    "PREVIOUS_WEEKEND_WORKED_END",
                    "CONSECUTIVE_WEEKENDS_WORKED"
            },
            MATRIX_COLUMN_NAMES;
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_ROSTERED_START = 1,
            COLUMN_INDEX_ROSTERED_END = 2,
            COLUMN_INDEX_LOGGED_START = 3,
            COLUMN_INDEX_LOGGED_END = 4,
            COLUMN_INDEX_LAST_SHIFT_ROSTERED_END = 5,
            COLUMN_INDEX_DURATION_OVER_DAY = 6,
            COLUMN_INDEX_DURATION_OVER_WEEK = 7,
            COLUMN_INDEX_DURATION_OVER_FORTNIGHT = 8,
            COLUMN_INDEX_CURRENT_WEEKEND_START = 9,
            COLUMN_INDEX_CURRENT_WEEKEND_END = 10,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START = 11,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END = 12,
            COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED = 13;

    static {
        MATRIX_COLUMN_NAMES = new String[RAW_PROJECTION.length + EXTRA_COLUMN_NAMES.length];
        System.arraycopy(RAW_PROJECTION, 0, MATRIX_COLUMN_NAMES, 0, RAW_PROJECTION.length);
        System.arraycopy(EXTRA_COLUMN_NAMES, 0, MATRIX_COLUMN_NAMES, RAW_PROJECTION.length, EXTRA_COLUMN_NAMES.length);
    }

    private Compliance() {
    }

    @NonNull
    static Cursor getCursor(SQLiteDatabase readableDatabase, @Nullable Long shiftId) {
        Cursor initialCursor = readableDatabase.query(
                ShiftContract.RosteredShifts.TABLE_NAME,
                RAW_PROJECTION,
                null,
                null,
                null,
                null,
                ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START
        );
        int initialCursorCount = initialCursor.getCount();
        MatrixCursor newCursor = new MatrixCursor(MATRIX_COLUMN_NAMES, shiftId == null ? initialCursorCount : 1);
        for (int i = 0; i < initialCursorCount; i++) {
            initialCursor.moveToPosition(i);
            long id = initialCursor.getLong(COLUMN_INDEX_ID);
            if (shiftId != null && shiftId != id) continue;
            Interval currentShift = new Interval(initialCursor.getLong(COLUMN_INDEX_ROSTERED_START), initialCursor.getLong(COLUMN_INDEX_ROSTERED_END));
            Interval loggedShift = (initialCursor.isNull(COLUMN_INDEX_LOGGED_START) || initialCursor.isNull(COLUMN_INDEX_LOGGED_END)) ?
                    null :
                    new Interval(initialCursor.getLong(COLUMN_INDEX_LOGGED_START), initialCursor.getLong(COLUMN_INDEX_LOGGED_END));
            MatrixCursor.RowBuilder builder = newCursor.newRow()
                    .add(id)
                    .add(currentShift.getStartMillis())
                    .add(currentShift.getEndMillis())
                    .add(loggedShift == null ? null : loggedShift.getStartMillis())
                    .add(loggedShift == null ? null : loggedShift.getEndMillis())
                    .add(initialCursor.moveToPrevious() ? initialCursor.getLong(COLUMN_INDEX_ROSTERED_END) : null)
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusDays(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(2).toInstant()).getMillis());
            Interval currentWeekend = getWeekend(currentShift);
            if (currentWeekend != null) {
                builder
                        .add(currentWeekend.getStartMillis())
                        .add(currentWeekend.getEndMillis());
                Interval previousWeekend = new Interval(currentWeekend.getStart().minusWeeks(1), currentWeekend.getEnd().minusWeeks(1));
                initialCursor.moveToPosition(i);
                while (initialCursor.moveToPrevious()) {
                    Interval weekendWorked = getWeekend(new Interval(initialCursor.getLong(COLUMN_INDEX_ROSTERED_START), initialCursor.getLong(COLUMN_INDEX_ROSTERED_END)));
                    if (weekendWorked != null && !currentWeekend.equals(weekendWorked)) {
                        builder
                                .add(weekendWorked.getStartMillis())
                                .add(weekendWorked.getEndMillis())
                                .add(weekendWorked.isEqual(previousWeekend) ? 1 : 0);
                        break;
                    }
                }
            }
            if (shiftId != null) break;
        }
        initialCursor.close();
        return newCursor;
    }

    @NonNull
    private static Duration getDurationSince(Cursor cursor, int positionToCheck, Instant cutOff) {
        Duration totalDuration = Duration.ZERO;
        cursor.moveToPosition(positionToCheck);
        do {
            Instant end = new Instant(cursor.getLong(COLUMN_INDEX_ROSTERED_END));
            if (!end.isAfter(cutOff)) break;
            Instant start = new Instant(cursor.getLong(COLUMN_INDEX_ROSTERED_START));
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

    public static class Wrapper extends CursorWrapper {
        public Wrapper(Cursor cursor) {
            super(cursor);
        }

        public long getId() {
            return getLong(COLUMN_INDEX_ID);
        }

        @NonNull
        public Interval getRosteredShift() {
            return new Interval(getLong(COLUMN_INDEX_ROSTERED_START), getLong(COLUMN_INDEX_ROSTERED_END));
        }

        @Nullable
        public Interval getLoggedShift() {
            return (isNull(COLUMN_INDEX_LOGGED_START) || isNull(COLUMN_INDEX_LOGGED_END)) ?
                    null :
                    new Interval(getLong(COLUMN_INDEX_LOGGED_START), getLong(COLUMN_INDEX_LOGGED_END));
        }

        @Nullable
        public Interval getIntervalBetweenShifts() {
            return isNull(COLUMN_INDEX_LAST_SHIFT_ROSTERED_END) ? null : new Interval(getLong(COLUMN_INDEX_LAST_SHIFT_ROSTERED_END), getLong(COLUMN_INDEX_ROSTERED_START));
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

    }

}
