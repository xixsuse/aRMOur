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
import org.joda.time.LocalDate;

public final class Compliance {


    public final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_ROSTERED_START = 1,
            COLUMN_INDEX_ROSTERED_END = 2,
            COLUMN_INDEX_LOGGED_START = 3,
            COLUMN_INDEX_LOGGED_END = 4;
    private final static int
            COLUMN_INDEX_DURATION_SINCE_LAST_SHIFT = 5,
            COLUMN_INDEX_DURATION_OVER_DAY = 6,
            COLUMN_INDEX_DURATION_OVER_WEEK = 7,
            COLUMN_INDEX_DURATION_OVER_FORTNIGHT = 8,
            COLUMN_INDEX_IS_WEEKEND = 9,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START = 10,
            COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED = 11;

    private static final String[] RAW_PROJECTION = new String[]{

            Contract.RosteredShifts._ID,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_START,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_END,
    },
            EXTRA_COLUMN_NAMES = new String[]{
                    "DURATION_SINCE_LAST_SHIFT",
                    "DURATION_OVER_DAY",
                    "DURATION_OVER_WEEK",
                    "DURATION_OVER_FORTNIGHT",
                    "IS_WEEKEND",
                    "PREVIOUS_WEEKEND_WORKED_START",
                    "CONSECUTIVE_WEEKENDS_WORKED"
            },
            MATRIX_COLUMN_NAMES;

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
                Contract.RosteredShifts.TABLE_NAME,
                RAW_PROJECTION,
                null,
                null,
                null,
                null,
                Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START
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
            Interval currentWeekend = getWeekend(currentShift);
            MatrixCursor.RowBuilder builder = newCursor.newRow()
                    .add(id)
                    .add(currentShift.getStartMillis())
                    .add(currentShift.getEndMillis())
                    .add(loggedShift == null ? null : loggedShift.getStartMillis())
                    .add(loggedShift == null ? null : loggedShift.getEndMillis())
                    .add(initialCursor.moveToPrevious() ? (currentShift.getStartMillis() - initialCursor.getLong(COLUMN_INDEX_ROSTERED_END)) : null)
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusDays(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(2).toInstant()).getMillis())
                    .add(currentWeekend == null ? 0 : 1);
            if (currentWeekend != null) {
                Interval previousWeekend = new Interval(currentWeekend.getStart().minusWeeks(1), currentWeekend.getEnd().minusWeeks(1));
                initialCursor.moveToPosition(i);
                while (initialCursor.moveToPrevious()) {
                    Interval weekendWorked = getWeekend(new Interval(initialCursor.getLong(COLUMN_INDEX_ROSTERED_START), initialCursor.getLong(COLUMN_INDEX_ROSTERED_END)));
                    if (weekendWorked != null && !currentWeekend.equals(weekendWorked)) {
                        builder
                                .add(weekendWorked.getStartMillis())
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
        public Duration getDurationBetweenShifts() {
            return isNull(COLUMN_INDEX_DURATION_SINCE_LAST_SHIFT) ? null : new Duration(getLong(COLUMN_INDEX_DURATION_SINCE_LAST_SHIFT));
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

        public boolean isWeekend() {
            return getInt(COLUMN_INDEX_IS_WEEKEND) == 1;
        }

        @Nullable
        public LocalDate getPreviousWeekend() {
            return isNull(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START) ? null : new LocalDate(getLong(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START));
        }

        public boolean consecutiveWeekendsWorked() {
            return getInt(COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED) == 1;
        }

    }

}
