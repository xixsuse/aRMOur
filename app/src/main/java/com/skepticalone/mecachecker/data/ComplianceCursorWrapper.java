package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.CursorWrapper;

public class ComplianceCursorWrapper extends CursorWrapper {
    public ComplianceCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public long getId(){
        return getLong(ComplianceCursor.COLUMN_INDEX_ID);
    }
    public long getStart(){
        return getLong(ComplianceCursor.COLUMN_INDEX_START);
    }
    public long getEnd(){
        return getLong(ComplianceCursor.COLUMN_INDEX_END);
    }
    public boolean startsAndEndsOnSameDay() {
        return getInt(ComplianceCursor.COLUMN_INDEX_STARTS_AND_ENDS_ON_SAME_DAY) == 1;
    }
    public long getDurationOfRest(){
        return getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OF_REST);
    }
    public long getDurationOverDay(){
        return getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_DAY);
    }
    public long getDurationOverWeek(){
        return getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_WEEK);
    }
    public long getDurationOverFortnight(){
        return getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_FORTNIGHT);
    }
    public boolean isWeekend(){
        return !isNull(ComplianceCursor.COLUMN_INDEX_CURRENT_WEEKEND_START) && !isNull(ComplianceCursor.COLUMN_INDEX_CURRENT_WEEKEND_END);
    }
    public long getCurrentWeekendStart(){
        return getLong(ComplianceCursor.COLUMN_INDEX_CURRENT_WEEKEND_START);
    }
    public long getCurrentWeekendEnd(){
        return getLong(ComplianceCursor.COLUMN_INDEX_CURRENT_WEEKEND_END);
    }
    public boolean previousWeekendWorked(){
        return !isNull(ComplianceCursor.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START) && !isNull(ComplianceCursor.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END);
    }
    public long getPreviousWeekendWorkedStart(){
        return getLong(ComplianceCursor.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START);
    }
    public long getPreviousWeekendWorkedEnd(){
        return getLong(ComplianceCursor.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END);
    }
    public boolean consecutiveWeekendsWorked(){
        return getInt(ComplianceCursor.COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED) == 1;
    }
}