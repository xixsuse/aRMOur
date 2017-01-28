package com.skepticalone.mecachecker.data;

import android.provider.BaseColumns;

public final class ShiftContract {
    private ShiftContract() {
    }

    public static class Shift implements BaseColumns {
        public static final String
                TABLE_NAME = "shifts",
                COLUMN_NAME_START = "start",
                COLUMN_NAME_END = "end",
                START_AS_DATE = "strftime('%d/%m/%Y', " + Shift.COLUMN_NAME_START + ", 'unixepoch', 'localtime')",
                START_AS_TIME = asTime(Shift.COLUMN_NAME_START),
                END_AS_TIME = asTime(Shift.COLUMN_NAME_END);
        static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_START + " INTEGER NOT NULL," +
                        COLUMN_NAME_END + " INTEGER NOT NULL)",
                SQL_DELETE_ENTRIES =
                        "DROP TABLE IF EXISTS " + TABLE_NAME;

        private static String asTime(String columnName) {
            return "strftime('%H:%M', " + columnName + ", 'unixepoch', 'localtime')";
        }
    }

}
