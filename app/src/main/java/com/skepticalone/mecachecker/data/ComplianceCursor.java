package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.util.Period;

import java.util.Calendar;

class ComplianceCursor extends MatrixCursor {
    final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_STARTS_AND_ENDS_ON_SAME_DAY = 3,
            COLUMN_INDEX_DURATION_OF_REST = 4,
            COLUMN_INDEX_DURATION_OVER_DAY = 5,
            COLUMN_INDEX_DURATION_OVER_WEEK = 6,
            COLUMN_INDEX_DURATION_OVER_FORTNIGHT = 7,
            COLUMN_INDEX_CURRENT_WEEKEND_START = 8,
            COLUMN_INDEX_CURRENT_WEEKEND_END = 9,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START = 10,
            COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END = 11,
            COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED = 12;
    final static String[] PROJECTION = new String[]{
            ShiftContract.Shift._ID,
            ShiftContract.Shift.COLUMN_NAME_START,
            ShiftContract.Shift.COLUMN_NAME_END
    };
    private final static String[]
            COLUMN_NAMES,
            EXTRA_COLUMN_NAMES = new String[]{
            "STARTS_AND_ENDS_ON_SAME_DAY",
            "DURATION_OF_REST",
            "DURATION_OVER_DAY",
            "DURATION_OVER_WEEK",
            "DURATION_OVER_FORTNIGHT",
            "CURRENT_WEEKEND_START",
            "CURRENT_WEEKEND_END",
            "PREVIOUS_WEEKEND_WORKED_START",
            "PREVIOUS_WEEKEND_WORKED_END",
            "CONSECUTIVE_WEEKENDS_WORKED"
    };
    static {
        COLUMN_NAMES = new String[PROJECTION.length + EXTRA_COLUMN_NAMES.length];
        System.arraycopy(PROJECTION, 0, COLUMN_NAMES, 0, PROJECTION.length);
        System.arraycopy(EXTRA_COLUMN_NAMES, 0, COLUMN_NAMES, PROJECTION.length, EXTRA_COLUMN_NAMES.length);
    }
    ComplianceCursor(@NonNull Cursor initialCursor, @Nullable Long shiftId) {
        super(COLUMN_NAMES, shiftId == null ? initialCursor.getCount() : 1);
        final int size = initialCursor.getCount();
        Calendar calendarToRecycle = Calendar.getInstance();
        for (int i = 0; i < size; i++) {
            initialCursor.moveToPosition(i);
            long id = initialCursor.getLong(COLUMN_INDEX_ID);
            if (shiftId != null && shiftId != id) continue;
            long start = initialCursor.getLong(COLUMN_INDEX_START), end = initialCursor.getLong(COLUMN_INDEX_END);
            calendarToRecycle.setTimeInMillis(start);
            int startDayOfMonth = calendarToRecycle.get(Calendar.DAY_OF_MONTH);
            calendarToRecycle.setTimeInMillis(end);
            boolean startsAndEndsOnSameDay = startDayOfMonth == calendarToRecycle.get(Calendar.DAY_OF_MONTH);
            RowBuilder builder = newRow()
                    .add(id)
                    .add(start)
                    .add(end)
                    .add(startsAndEndsOnSameDay ? 1 : 0)
                    .add(getDurationOfRest(initialCursor, i))
                    .add(getDurationOverDay(initialCursor, calendarToRecycle, i))
                    .add(getDurationOverWeek(initialCursor, calendarToRecycle, i))
                    .add(getDurationOverFortnight(initialCursor, calendarToRecycle, i));
            Period weekend = getWeekend(initialCursor, calendarToRecycle, i);
            if (weekend != null) {
                builder
                        .add(weekend.start)
                        .add(weekend.end);
                Period previousWeekendWorked = getLastWeekendWorked(initialCursor, calendarToRecycle, i, weekend);
                if (previousWeekendWorked != null) {
                    builder
                            .add(previousWeekendWorked.start)
                            .add(previousWeekendWorked.end)
                            .add(weekend.advanceDays(calendarToRecycle, -7).equals(previousWeekendWorked) ? 1 : 0);
                }
            }
            if (shiftId != null) break;
        }
        initialCursor.close();
    }

    private static long getDurationOverDay(Cursor cursor, Calendar calendarToRecycle, int positionToCheck) {
        return getDurationOverPeriod(cursor, calendarToRecycle, positionToCheck, 1);
    }

    private static long getDurationOverWeek(Cursor cursor, Calendar calendarToRecycle, int positionToCheck) {
        return getDurationOverPeriod(cursor, calendarToRecycle, positionToCheck, 7);
    }

    private static long getDurationOverFortnight(Cursor cursor, Calendar calendarToRecycle, int positionToCheck) {
        return getDurationOverPeriod(cursor, calendarToRecycle, positionToCheck, 14);
    }

    private static long getDurationOverPeriod(Cursor cursor, Calendar calendarToRecycle, int positionToCheck, int periodInDays) {
        cursor.moveToPosition(positionToCheck);
        calendarToRecycle.setTimeInMillis(cursor.getLong(COLUMN_INDEX_END));
        calendarToRecycle.add(Calendar.DATE, -periodInDays);
        long periodStart = calendarToRecycle.getTimeInMillis(), totalDuration = 0;
        do {
            long end = cursor.getLong(COLUMN_INDEX_END);
            if (periodStart >= end) break;
            totalDuration += end - Math.max(periodStart, cursor.getLong(COLUMN_INDEX_START));
        } while (cursor.moveToPrevious());
        return totalDuration;
    }

    private static long getDurationOfRest(Cursor cursor, int positionToCheck) {
        cursor.moveToPosition(positionToCheck);
        long currentStart = cursor.getLong(COLUMN_INDEX_START);
        if (cursor.moveToPrevious()) {
            long previousEnd = cursor.getLong(COLUMN_INDEX_END);
            return currentStart - previousEnd;
        } else return -1L;
    }

    @Nullable
    private static Period getWeekend(Cursor cursor, Calendar calendarToRecycle, int positionToCheck) {
        cursor.moveToPosition(positionToCheck);
        return Period.getWeekend(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END), calendarToRecycle);
    }

    @Nullable
    private static Period getLastWeekendWorked(Cursor cursor, Calendar calendarToRecycle, int positionToCheck, @NonNull Period currentWeekend) {
        for (int i = positionToCheck - 1; i >= 0; i--) {
            Period weekend = getWeekend(cursor, calendarToRecycle, i);
            if (weekend != null && !weekend.equals(currentWeekend)) {
                return weekend;
            }
        }
        return null;
    }

}
