package com.skepticalone.mecachecker.data;

import android.provider.BaseColumns;

final class Contract {
    static final String
            COLUMN_NAME_PAYMENT = "payment",
            COLUMN_NAME_CLAIMED = "claimed",
            COLUMN_NAME_PAID = "paid",
            COLUMN_NAME_COMMENT = "comment";

    private Contract() {
    }

    private static String dropTable(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }
//    private static String createTrigger(boolean insert, String tableName, String columnNameStart, String columnNameEnd) {
//        return "CREATE TRIGGER " + tableName + (insert ? "_insert_trigger " : "_update_trigger ") +
//                "BEFORE " + (insert ? "INSERT" : "UPDATE") + " ON " + tableName + " BEGIN " +
//                "SELECT CASE WHEN EXISTS (" +
//                "SELECT " + BaseColumns._ID + " FROM " + tableName + " WHERE " +
//                (insert ? "" : (BaseColumns._ID + " != OLD." + BaseColumns._ID + " AND ")) +
//                columnNameStart + " < NEW." + columnNameEnd + " AND " +
//                "NEW." + columnNameStart + " < " + columnNameEnd +
//                ") THEN RAISE (ABORT, 'Overlapping shifts') END; " +
//                "END";
//    }

    //    public static class RosteredShifts implements BaseColumns {
//        public static final String
//                COLUMN_NAME_ROSTERED_START = "rostered_start",
//                COLUMN_NAME_ROSTERED_END = "rostered_end",
//                COLUMN_NAME_LOGGED_START = "logged_start",
//                COLUMN_NAME_LOGGED_END = "logged_end";
//        static final String TABLE_NAME = "rostered_shifts",
//                SQL_CREATE_TABLE = "CREATE TABLE " +
//                        TABLE_NAME +
//                        " (" +
//                        _ID + " INTEGER PRIMARY KEY, " +
//                        COLUMN_NAME_ROSTERED_START + " INTEGER NOT NULL, " +
//                        COLUMN_NAME_ROSTERED_END + " INTEGER NOT NULL, " +
//                        COLUMN_NAME_LOGGED_START + " INTEGER DEFAULT NULL, " +
//                        COLUMN_NAME_LOGGED_END + " INTEGER DEFAULT NULL, " +
//                        "CHECK (" + COLUMN_NAME_ROSTERED_START + " < " + COLUMN_NAME_ROSTERED_END + "), " +
//                        "CHECK ((" + COLUMN_NAME_LOGGED_START + " IS NULL AND " + COLUMN_NAME_LOGGED_END + " IS NULL) OR (" +
//                        COLUMN_NAME_LOGGED_START + " < " + COLUMN_NAME_LOGGED_END + "))" +
//                        ")",
//                SQL_CREATE_INDEX_START = "CREATE INDEX " + COLUMN_NAME_ROSTERED_START + "_index ON " + TABLE_NAME + " (" + COLUMN_NAME_ROSTERED_START + ")",
//                SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true, TABLE_NAME, COLUMN_NAME_ROSTERED_START, COLUMN_NAME_ROSTERED_END),
//                SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false, TABLE_NAME, COLUMN_NAME_ROSTERED_START, COLUMN_NAME_ROSTERED_END),
//                SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//    public static class AdditionalShifts implements BaseColumns {
//        public static final String
//                COLUMN_NAME_START = "start",
//                COLUMN_NAME_END = "end",
//                COLUMN_NAME_RATE = "rate",
//                COLUMN_NAME_CLAIMED = "claimed",
//                COLUMN_NAME_PAID = "paid",
//                COLUMN_NAME_COMMENT = "comment";
//        static final String TABLE_NAME = "additional_shifts",
//                SQL_CREATE_TABLE = "CREATE TABLE " +
//                        TABLE_NAME +
//                        " (" +
//                        _ID + " INTEGER PRIMARY KEY, " +
//                        COLUMN_NAME_START + " INTEGER NOT NULL, " +
//                        COLUMN_NAME_END + " INTEGER NOT NULL, " +
//                        COLUMN_NAME_RATE + " INTEGER NOT NULL, " +
//                        COLUMN_NAME_CLAIMED + " INTEGER DEFAULT NULL, " +
//                        COLUMN_NAME_PAID + " INTEGER DEFAULT NULL, " +
//                        COLUMN_NAME_COMMENT + " TEXT DEFAULT NULL, " +
//                        "CHECK (" + COLUMN_NAME_START + " < " + COLUMN_NAME_END + "), " +
//                        "CHECK (" + COLUMN_NAME_PAID + " IS NULL OR " + COLUMN_NAME_CLAIMED + " IS NOT NULL), " +
//                        "CHECK (" + COLUMN_NAME_CLAIMED + " <= " + COLUMN_NAME_PAID + "), " +
//                        "CHECK (length(" + COLUMN_NAME_COMMENT + ") > 0)" +
//                        ")",
//                SQL_CREATE_INDEX_START = "CREATE INDEX " + COLUMN_NAME_START + "_index ON " + TABLE_NAME + " (" + COLUMN_NAME_START + ")",
//                SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true, TABLE_NAME, COLUMN_NAME_START, COLUMN_NAME_END),
//                SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false, TABLE_NAME, COLUMN_NAME_START, COLUMN_NAME_END),
//                SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
    static class CrossCoverShifts implements BaseColumns {
        static final String
                TABLE_NAME = "cross_cover",
                COLUMN_NAME_DATE = "date",
                SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_DATE + " INTEGER UNIQUE NOT NULL, " +
                COLUMN_NAME_PAYMENT + " INTEGER NOT NULL, " +
                COLUMN_NAME_CLAIMED + " INTEGER DEFAULT NULL, " +
                COLUMN_NAME_PAID + " INTEGER DEFAULT NULL, " +
                COLUMN_NAME_COMMENT + " TEXT DEFAULT NULL, " +
                "CHECK (" + COLUMN_NAME_PAID + " IS NULL OR " + COLUMN_NAME_CLAIMED + " IS NOT NULL), " +
                "CHECK (" + COLUMN_NAME_CLAIMED + " <= " + COLUMN_NAME_PAID + "), " +
                "CHECK (length(" + COLUMN_NAME_COMMENT + ") > 0)" +
                ")",
                SQL_DROP_TABLE = dropTable(TABLE_NAME);
    }

    static class Expenses implements BaseColumns {
        static final String
                TABLE_NAME = "expenses",
                COLUMN_NAME_TITLE = "title",
                SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                COLUMN_NAME_PAYMENT + " INTEGER NOT NULL, " +
                COLUMN_NAME_CLAIMED + " INTEGER DEFAULT NULL, " +
                COLUMN_NAME_PAID + " INTEGER DEFAULT NULL, " +
                COLUMN_NAME_COMMENT + " TEXT DEFAULT NULL, " +
                "CHECK (length(" + COLUMN_NAME_TITLE + ") > 0), " +
                "CHECK (" + COLUMN_NAME_PAID + " IS NULL OR " + COLUMN_NAME_CLAIMED + " IS NOT NULL), " +
                "CHECK (" + COLUMN_NAME_CLAIMED + " <= " + COLUMN_NAME_PAID + "), " +
                "CHECK (length(" + COLUMN_NAME_COMMENT + ") > 0)" +
                ")",
                SQL_DROP_TABLE = dropTable(TABLE_NAME);
    }
}
