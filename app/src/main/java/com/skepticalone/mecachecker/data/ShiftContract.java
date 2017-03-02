package com.skepticalone.mecachecker.data;

import android.provider.BaseColumns;

final class ShiftContract {
    private ShiftContract() {
    }
    static class Shift implements BaseColumns {
        static final String
                TABLE_NAME = "shifts",
                COLUMN_NAME_START = "start",
                COLUMN_NAME_END = "end",
                COLUMN_NAME_CATEGORY = "category",
                SQL_CREATE_ENTRIES = "CREATE TABLE " +
                        TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_START + " INTEGER NOT NULL, " +
                        COLUMN_NAME_END + " INTEGER NOT NULL, " +
                        COLUMN_NAME_CATEGORY + " INTEGER NOT NULL, " +
                        "CHECK (" + COLUMN_NAME_START + " < " + COLUMN_NAME_END + ")" +
                        ")",
                SQL_CREATE_START_INDEX = createIndex(COLUMN_NAME_START),
                SQL_CREATE_CATEGORY_INDEX = createIndex(COLUMN_NAME_CATEGORY),
                SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true),
                SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false),
                SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private final static String ERROR_MESSAGE_ON_OVERLAP = "Overlapping shifts";

        private static String createIndex(String columnName) {
            return "CREATE INDEX " +
                    columnName +
                    "_index" +
                    " ON " +
                    TABLE_NAME +
                    " (" +
                    columnName +
                    ")";
        }

        private static String createTrigger(boolean insert) {
            return "CREATE TRIGGER " + (insert ? "insert" : "update") + "_trigger " +
                    "BEFORE " + (insert ? "INSERT" : "UPDATE") + " ON " + TABLE_NAME + " BEGIN " +
                    "SELECT CASE WHEN EXISTS (" +
                    "SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE " +
                    COLUMN_NAME_CATEGORY + " == NEW." + COLUMN_NAME_CATEGORY + " AND " +
                    (insert ? "" : (_ID + " != OLD." + _ID + " AND ")) +
                    COLUMN_NAME_START + " < NEW." + COLUMN_NAME_END + " AND " +
                    "NEW." + COLUMN_NAME_START + " < " + COLUMN_NAME_END +
                    ") " +
                    "THEN RAISE (ABORT, '" + ERROR_MESSAGE_ON_OVERLAP + "') END; " +
                    "END";
        }
    }

    static class Compliance {
        static final String[] PROJECTION = new String[]{
                ShiftContract.Shift._ID,
                ShiftContract.Shift.COLUMN_NAME_START,
                ShiftContract.Shift.COLUMN_NAME_END
        };
        final static String[]
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
        final static int
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
        final static int
                SHIFT_TYPE_NORMAL_DAY = 1,
                SHIFT_TYPE_LONG_DAY = 2,
                SHIFT_TYPE_NIGHT_SHIFT = 3,
                SHIFT_TYPE_OTHER = 4;

        static {
            COLUMN_NAMES = new String[PROJECTION.length + EXTRA_COLUMN_NAMES.length];
            System.arraycopy(PROJECTION, 0, COLUMN_NAMES, 0, PROJECTION.length);
            System.arraycopy(EXTRA_COLUMN_NAMES, 0, COLUMN_NAMES, PROJECTION.length, EXTRA_COLUMN_NAMES.length);
        }

    }
}
