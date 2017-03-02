package com.skepticalone.mecachecker.data;

import android.provider.BaseColumns;

public final class ShiftContract {
    private ShiftContract() {
    }

    public static class RosteredShift implements BaseColumns {
        public static final String
                COLUMN_NAME_SCHEDULED_START = "scheduled_start",
                COLUMN_NAME_SCHEDULED_END = "scheduled_end",
                COLUMN_NAME_LOGGED_START = "logged_start",
                COLUMN_NAME_LOGGED_END = "logged_end";

        static final String
                TABLE_NAME = "rostered_shifts",
                SQL_CREATE_TABLE = "CREATE TABLE " +
                        TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_SCHEDULED_START + " INTEGER NOT NULL, " +
                        COLUMN_NAME_SCHEDULED_END + " INTEGER NOT NULL, " +
                        COLUMN_NAME_LOGGED_START + " INTEGER DEFAULT NULL, " +
                        COLUMN_NAME_LOGGED_END + " INTEGER DEFAULT NULL, " +
                        "CHECK (" + COLUMN_NAME_SCHEDULED_START + " < " + COLUMN_NAME_SCHEDULED_END + "), " +
                        "CHECK ((" + COLUMN_NAME_LOGGED_START + " IS NULL AND " + COLUMN_NAME_LOGGED_END + " IS NULL) OR (" +
                        COLUMN_NAME_LOGGED_START + " < " + COLUMN_NAME_LOGGED_END + "))" +
                        ")",
                SQL_CREATE_INDEX_START = "CREATE INDEX " + COLUMN_NAME_SCHEDULED_START + "_index ON " + TABLE_NAME + " (" + COLUMN_NAME_SCHEDULED_START + ")",
                SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true),
                SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false),
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private final static String ERROR_MESSAGE_ON_OVERLAP = "Overlapping shifts";

        private static String createTrigger(boolean insert) {
            return "CREATE TRIGGER " + (insert ? "insert" : "update") + "_trigger " +
                    "BEFORE " + (insert ? "INSERT" : "UPDATE") + " ON " + TABLE_NAME + " BEGIN " +
                    "SELECT CASE WHEN EXISTS (" +
                    "SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE " +
//                    COLUMN_NAME_CATEGORY + " == NEW." + COLUMN_NAME_CATEGORY + " AND " +
                    (insert ? "" : (_ID + " != OLD." + _ID + " AND ")) +
                    "((" + COLUMN_NAME_SCHEDULED_START + " < NEW." + COLUMN_NAME_SCHEDULED_END + " AND " +
                    "NEW." + COLUMN_NAME_SCHEDULED_START + " < " + COLUMN_NAME_SCHEDULED_END + ") OR (" +
                    COLUMN_NAME_LOGGED_START + " < NEW." + COLUMN_NAME_LOGGED_END + " AND " +
                    "NEW." + COLUMN_NAME_LOGGED_START + " < " + COLUMN_NAME_LOGGED_END + "))" +
                    ") " +
                    "THEN RAISE (ABORT, '" + ERROR_MESSAGE_ON_OVERLAP + "') END; " +
                    "END";
        }
    }

    static class Compliance {
        static final String[] PROJECTION = new String[]{
                RosteredShift._ID,
                RosteredShift.COLUMN_NAME_SCHEDULED_START,
                RosteredShift.COLUMN_NAME_SCHEDULED_END
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
