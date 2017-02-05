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
                START_AS_DATE = asDate(Shift.COLUMN_NAME_START),
                START_AS_TIME = asTime(Shift.COLUMN_NAME_START),
                END_AS_TIME = asTime(Shift.COLUMN_NAME_END),
                SQL_CREATE_ENTRIES = "CREATE TABLE " +
                        TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_START + " INTEGER NOT NULL, " +
                        COLUMN_NAME_END + " INTEGER NOT NULL, " +
                        "CHECK (" + COLUMN_NAME_START + " < " + COLUMN_NAME_END + ")" +
                        ")",
                SQL_CREATE_TRIGGER_BEFORE_INSERT = getTriggerProgram(true),
                SQL_CREATE_TRIGGER_BEFORE_UPDATE = getTriggerProgram(false),
                SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private final static String ERROR_MESSAGE_ON_OVERLAP = "Overlapping shifts";
        private static String getTriggerProgram(boolean insert) {
            return "CREATE TRIGGER " +
                    (insert ? "insert" : "update") +
                    "_trigger BEFORE " +
                    (insert ? "INSERT" : "UPDATE") +
                    " ON " +
                    TABLE_NAME +
                    " BEGIN SELECT CASE WHEN EXISTS (SELECT " +
                    _ID +
                    " FROM " +
                    TABLE_NAME +
                    " WHERE " +
                    (insert ? "" : (_ID + " != OLD." + _ID + " AND ")) +
                    "NOT (" +
                    COLUMN_NAME_END +
                    " <= NEW." +
                    COLUMN_NAME_START +
                    " OR " +
                    COLUMN_NAME_START +
                    " >= NEW." +
                    COLUMN_NAME_END +
                    ")) THEN RAISE (ABORT, '" +
                    ERROR_MESSAGE_ON_OVERLAP +
                    "') END; END";
        }
        private static String asTime(String columnName) {
            return formatTime(columnName, "%H:%M");
        }

        private static String asDate(@SuppressWarnings("SameParameterValue") String columnName) {
            return formatTime(columnName, "%d/%m/%Y");
        }

        @SuppressWarnings("SpellCheckingInspection")
        private static String formatTime(String columnName, String formatString) {
            return "strftime('" + formatString + "'," + columnName + ",'unixepoch','localtime')";
        }
    }
}
