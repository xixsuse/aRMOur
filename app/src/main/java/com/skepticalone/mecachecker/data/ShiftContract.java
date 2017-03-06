package com.skepticalone.mecachecker.data;

import android.provider.BaseColumns;

public final class ShiftContract {
    private ShiftContract() {
    }

    public static class RosteredShift implements BaseColumns {
        public static final String
                COLUMN_NAME_ROSTERED_START = "rostered_start",
                COLUMN_NAME_ROSTERED_END = "rostered_end",
                COLUMN_NAME_LOGGED_START = "logged_start",
                COLUMN_NAME_LOGGED_END = "logged_end";
        static final String TABLE_NAME = "rostered_shifts",
                SQL_CREATE_TABLE = "CREATE TABLE " +
                        TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_ROSTERED_START + " INTEGER NOT NULL, " +
                        COLUMN_NAME_ROSTERED_END + " INTEGER NOT NULL, " +
                        COLUMN_NAME_LOGGED_START + " INTEGER DEFAULT NULL, " +
                        COLUMN_NAME_LOGGED_END + " INTEGER DEFAULT NULL, " +
                        "CHECK (" + COLUMN_NAME_ROSTERED_START + " < " + COLUMN_NAME_ROSTERED_END + "), " +
                        "CHECK ((" + COLUMN_NAME_LOGGED_START + " IS NULL AND " + COLUMN_NAME_LOGGED_END + " IS NULL) OR (" +
                        COLUMN_NAME_LOGGED_START + " < " + COLUMN_NAME_LOGGED_END + "))" +
                        ")",
                SQL_CREATE_INDEX_START = "CREATE INDEX " + COLUMN_NAME_ROSTERED_START + "_index ON " + TABLE_NAME + " (" + COLUMN_NAME_ROSTERED_START + ")",
                SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true),
                SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false),
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        private static String createTrigger(boolean insert) {
            return "CREATE TRIGGER " + (insert ? "insert" : "update") + "_trigger " +
                    "BEFORE " + (insert ? "INSERT" : "UPDATE") + " ON " + TABLE_NAME + " BEGIN " +
                    "SELECT CASE WHEN EXISTS (" +
                    "SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE " +
                    (insert ? "" : (_ID + " != OLD." + _ID + " AND ")) +
                    "((" + COLUMN_NAME_ROSTERED_START + " < NEW." + COLUMN_NAME_ROSTERED_END + " AND " +
                    "NEW." + COLUMN_NAME_ROSTERED_START + " < " + COLUMN_NAME_ROSTERED_END + ") OR (" +
                    COLUMN_NAME_LOGGED_START + " < NEW." + COLUMN_NAME_LOGGED_END + " AND " +
                    "NEW." + COLUMN_NAME_LOGGED_START + " < " + COLUMN_NAME_LOGGED_END + "))" +
                    ") " +
                    "THEN RAISE (ABORT, 'Overlapping shifts') END; " +
                    "END";
        }
    }

}
