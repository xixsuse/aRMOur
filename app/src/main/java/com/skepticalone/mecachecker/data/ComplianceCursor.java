package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.Compliance;

import java.util.Calendar;


public class ComplianceCursor extends MatrixCursor {

    public final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_DURATION_OF_REST = 3,
            COLUMN_INDEX_DURATION_OVER_DAY = 4,
            COLUMN_INDEX_DURATION_OVER_WEEK = 5,
            COLUMN_INDEX_DURATION_OVER_FORTNIGHT = 6,
            COLUMN_INDEX_IS_WEEKEND = 7,
            COLUMN_INDEX_LAST_WEEKEND_WORKED_START = 8,
            COLUMN_INDEX_LAST_WEEKEND_WORKED_END = 9;
    final static String[] PROJECTION = new String[]{
            ShiftContract.Shift._ID,
            ShiftContract.Shift.COLUMN_NAME_START,
            ShiftContract.Shift.COLUMN_NAME_END
    };
    private final static String[] COLUMN_NAMES;
    private final static String[] EXTRA_COLUMN_NAMES = new String[]{
            "DURATION_OF_REST",
            "DURATION_OVER_DAY",
            "DURATION_OVER_WEEK",
            "DURATION_OVER_FORTNIGHT",
            "IS_WEEKEND",
            "LAST_WEEKEND_WORKED_START",
            "LAST_WEEKEND_WORKED_END"
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
            if (shiftId != null && shiftId != id) {
                continue;
            }
            newRow()
                    .add(id)
                    .add(initialCursor.getLong(COLUMN_INDEX_START))
                    .add(initialCursor.getLong(COLUMN_INDEX_END))
                    .add(Compliance.getDurationOfRest(initialCursor, COLUMN_INDEX_START, COLUMN_INDEX_END, i))
                    .add(Compliance.getDurationOverDay(initialCursor, COLUMN_INDEX_START, COLUMN_INDEX_END, calendarToRecycle, i, true))
                    .add(Compliance.getDurationOverWeek(initialCursor, COLUMN_INDEX_START, COLUMN_INDEX_END, calendarToRecycle, i, true))
                    .add(Compliance.getDurationOverFortnight(initialCursor, COLUMN_INDEX_START, COLUMN_INDEX_END, calendarToRecycle, i, true))
                    .add(Compliance.isWeekendShift(initialCursor, COLUMN_INDEX_START, COLUMN_INDEX_END, calendarToRecycle, i) ? 1 : 0)
            ;
            if (shiftId != null) {
                break;
            }

        }
//        if (initialCursor.moveToFirst()) {
//            do {
//                PeriodWithStableId shift = new PeriodWithStableId(
//                        initialCursor.getLong(COLUMN_INDEX_START),
//                        initialCursor.getLong(COLUMN_INDEX_END),
//                        initialCursor.getLong(COLUMN_INDEX_ID)
//                );
//                mShifts.add(shift);
//            } while (initialCursor.moveToNext());
//            Compliance.checkMinimumRestHoursBetweenShifts(mShifts);
//            Compliance.checkMaximumHoursPerDay(mShifts);
//            Compliance.checkMaximumHoursPerWeek(mShifts);
//            Compliance.checkMaximumHoursPerFortnight(mShifts);
//            Compliance.checkMaximumConsecutiveWeekends(mShifts);
//            for (PeriodWithStableId shift : mShifts) {
//                if (shiftId != null && shiftId != shift.getId()) {
//                    continue;
//                }
//                newRow()
//                        .add(shift.getId())
//                        .add(shift.getStart().getTime())
//                        .add(shift.getEnd().getTime())
//                        .add(shift.isCompliantWithMaximumHoursPerDay() ? 1 : 0);
//                if (shiftId != null) {
//                    break;
//                }
//            }
//        }
        initialCursor.close();
    }
}
