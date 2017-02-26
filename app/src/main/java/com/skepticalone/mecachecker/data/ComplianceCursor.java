package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.components.TimePreference;
import com.skepticalone.mecachecker.util.Period;

import java.util.Calendar;

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
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_SHIFT_TYPE = 3,
            COLUMN_INDEX_DURATION_OF_REST = 4,
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

    public long getStart() {
        return getLong(COLUMN_INDEX_START);
    }

    public long getEnd() {
        return getLong(COLUMN_INDEX_END);
    }

    public int getShiftType() {
        return getInt(COLUMN_INDEX_SHIFT_TYPE);
    }

    public long getDurationOfRest() {
        return getLong(COLUMN_INDEX_DURATION_OF_REST);
    }

    public long getDurationOverDay() {
        return getLong(COLUMN_INDEX_DURATION_OVER_DAY);
    }

    public long getDurationOverWeek() {
        return getLong(COLUMN_INDEX_DURATION_OVER_WEEK);
    }

    public long getDurationOverFortnight() {
        return getLong(COLUMN_INDEX_DURATION_OVER_FORTNIGHT);
    }

    public boolean isWeekend() {
        return !isNull(COLUMN_INDEX_CURRENT_WEEKEND_START) && !isNull(COLUMN_INDEX_CURRENT_WEEKEND_END);
    }

    public long getCurrentWeekendStart() {
        return getLong(COLUMN_INDEX_CURRENT_WEEKEND_START);
    }

    public long getCurrentWeekendEnd() {
        return getLong(COLUMN_INDEX_CURRENT_WEEKEND_END);
    }

    public boolean previousWeekendWorked() {
        return !isNull(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START) && !isNull(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END);
    }

    public long getPreviousWeekendWorkedStart() {
        return getLong(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START);
    }

    public long getPreviousWeekendWorkedEnd() {
        return getLong(COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END);
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
            Calendar calendarToRecycle = Calendar.getInstance();
            for (int i = 0, size = initialCursor.getCount(); i < size; i++) {
                initialCursor.moveToPosition(i);
                long id = initialCursor.getLong(COLUMN_INDEX_ID);
                if (shiftId != null && shiftId != id) continue;
                long start = initialCursor.getLong(COLUMN_INDEX_START), end = initialCursor.getLong(COLUMN_INDEX_END);
                calendarToRecycle.setTimeInMillis(start);
                int startTotalMinutes = TimePreference.calculateTotalMinutes(calendarToRecycle.get(Calendar.HOUR_OF_DAY), calendarToRecycle.get(Calendar.MINUTE));
                calendarToRecycle.setTimeInMillis(end);
                int endTotalMinutes = TimePreference.calculateTotalMinutes(calendarToRecycle.get(Calendar.HOUR_OF_DAY), calendarToRecycle.get(Calendar.MINUTE));
                int shiftType;
                if (startTotalMinutes == normalDayStartTotalMinutes && endTotalMinutes == normalDayEndTotalMinutes) {
                    shiftType = SHIFT_TYPE_NORMAL_DAY;
                } else if (startTotalMinutes == longDayStartTotalMinutes && endTotalMinutes == longDayEndTotalMinutes) {
                    shiftType = SHIFT_TYPE_LONG_DAY;
                } else if (startTotalMinutes == nightShiftStartTotalMinutes && endTotalMinutes == nightShiftEndTotalMinutes) {
                    shiftType = SHIFT_TYPE_NIGHT_SHIFT;
                } else {
                    shiftType = SHIFT_TYPE_OTHER;
                }
                MatrixCursor.RowBuilder builder = newRow()
                        .add(id)
                        .add(start)
                        .add(end)
                        .add(shiftType)
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
}
