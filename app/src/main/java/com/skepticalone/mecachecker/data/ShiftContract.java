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
}
