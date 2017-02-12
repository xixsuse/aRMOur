package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.Compliance;
import com.skepticalone.mecachecker.PeriodWithStableId;

import java.util.ArrayList;
import java.util.List;


public class ComplianceCursor extends MatrixCursor {

    public final static int COLUMN_INDEX_ID = 0;
    public final static int COLUMN_INDEX_START = 1;
    public final static int COLUMN_INDEX_END = 2;
    public final static int COLUMN_INDEX_MAX_HOURS = 3;
    final static String[] PROJECTION = new String[]{
            ShiftContract.Shift._ID,
            ShiftContract.Shift.COLUMN_NAME_START,
            ShiftContract.Shift.COLUMN_NAME_END
    };
    private final static String[] COLUMN_NAMES;
    private final static String[] EXTRA_COLUMN_NAMES = new String[]{
            "MAX_HOURS"
    };

    static {
        COLUMN_NAMES = new String[PROJECTION.length + EXTRA_COLUMN_NAMES.length];
        System.arraycopy(PROJECTION, 0, COLUMN_NAMES, 0, PROJECTION.length);
        System.arraycopy(EXTRA_COLUMN_NAMES, 0, COLUMN_NAMES, PROJECTION.length, EXTRA_COLUMN_NAMES.length);
    }

    ComplianceCursor(@NonNull Cursor initialCursor, @Nullable Long shiftId) {
        super(COLUMN_NAMES, shiftId == null ? initialCursor.getCount() : 1);
        List<PeriodWithStableId> mShifts = new ArrayList<>();
        mShifts.clear();
        if (initialCursor.moveToFirst()) {
            do {
                PeriodWithStableId shift = new PeriodWithStableId(
                        initialCursor.getLong(COLUMN_INDEX_START),
                        initialCursor.getLong(COLUMN_INDEX_END),
                        initialCursor.getLong(COLUMN_INDEX_ID)
                );
                mShifts.add(shift);
            } while (initialCursor.moveToNext());
            Compliance.checkMinimumRestHoursBetweenShifts(mShifts);
            Compliance.checkMaximumHoursPerDay(mShifts);
            Compliance.checkMaximumHoursPerWeek(mShifts);
            Compliance.checkMaximumHoursPerFortnight(mShifts);
            Compliance.checkMaximumConsecutiveWeekends(mShifts);
            for (PeriodWithStableId shift : mShifts) {
                if (shiftId != null && shiftId != shift.getId()) {
                    continue;
                }
                newRow()
                        .add(shift.getId())
                        .add(shift.getStart().getTime())
                        .add(shift.getEnd().getTime())
                        .add(shift.isCompliantWithMaximumHoursPerDay() ? 1 : 0);
                if (shiftId != null) {
                    break;
                }
            }
        }
        initialCursor.close();
    }
}
