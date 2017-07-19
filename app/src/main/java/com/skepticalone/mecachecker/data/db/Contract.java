package com.skepticalone.mecachecker.data.db;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

public final class Contract {
    public static final String
            COLUMN_NAME_PAYMENT = "payment",
            COLUMN_NAME_CLAIMED = "claimed",
            COLUMN_NAME_PAID = "paid",
            COLUMN_NAME_COMMENT = "comment",
            COLUMN_NAME_SHIFT_START = "shift_start",
            COLUMN_NAME_SHIFT_END = "shift_end",
            BASE_COLUMN_DEFINITIONS =
                    BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                            COLUMN_NAME_COMMENT + " TEXT DEFAULT NULL",
            BASE_CHECKS = checkHasLength(COLUMN_NAME_COMMENT),
            SHIFT_COLUMN_DEFINITIONS =
                    COLUMN_NAME_SHIFT_START + " INTEGER NOT NULL, " +
                            COLUMN_NAME_SHIFT_END + " INTEGER NOT NULL",
            SHIFT_CHECKS = "CHECK (" + COLUMN_NAME_SHIFT_START + " <= " + COLUMN_NAME_SHIFT_END + ")",
            PAYABLE_COLUMN_DEFINITIONS =
                    COLUMN_NAME_PAYMENT + " INTEGER NOT NULL, " +
                            COLUMN_NAME_CLAIMED + " INTEGER DEFAULT NULL, " +
                            COLUMN_NAME_PAID + " INTEGER DEFAULT NULL",
            PAYABLE_CHECKS =
                    "CHECK (" + COLUMN_NAME_PAID + " IS NULL OR (" + COLUMN_NAME_CLAIMED + " IS NOT NULL AND " + COLUMN_NAME_CLAIMED + " <= " + COLUMN_NAME_PAID + "))";
    private static final String INDEX_PREFIX = "index_";

    private Contract() {
    }

    private static String checkHasLength(String columnName) {
        return "CHECK (length(" + columnName + ") > 0)";
    }

    private static String createIndex(String indexName, boolean unique, String tableName, @NonNull String indexedColumn) {
        return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " ON " + tableName + " (" + indexedColumn + ")";
    }
    private static String dropTable(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    private static String getOverlappingShiftCriteria(String columnNameStart, String columnNameEnd) {
        return columnNameStart + " < NEW." + columnNameEnd + " AND NEW." + columnNameStart + " < " + columnNameEnd;
    }

    private static String createTrigger(boolean insert, String tableName, String overlappingShiftCriteria) {
        return "CREATE TRIGGER " + tableName + (insert ? "_insert_trigger " : "_update_trigger ") +
                "BEFORE " + (insert ? "INSERT" : "UPDATE") + " ON " + tableName + " BEGIN " +
                "SELECT CASE WHEN EXISTS (" +
                "SELECT " + BaseColumns._ID + " FROM " + tableName + " WHERE " +
                (insert ? "" : (BaseColumns._ID + " != OLD." + BaseColumns._ID + " AND ")) +
                "(" + overlappingShiftCriteria + ")" +
                ") THEN RAISE (ABORT, 'Overlapping shifts') " +
                "END; END";
    }

    public static class RosteredShifts {
        private static final String
                OVERLAPPING_SHIFT_CRITERIA;
        public static final String
                TABLE_NAME = "rostered_shifts",
                INDEX = INDEX_PREFIX + TABLE_NAME,
                LOGGED_PREFIX = "logged_",
                COLUMN_NAME_LOGGED_SHIFT_START = LOGGED_PREFIX + COLUMN_NAME_SHIFT_START,
                COLUMN_NAME_LOGGED_SHIFT_END = LOGGED_PREFIX + COLUMN_NAME_SHIFT_END,
                SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                        BASE_COLUMN_DEFINITIONS + ", " +
                        SHIFT_COLUMN_DEFINITIONS + ", " +
                        COLUMN_NAME_LOGGED_SHIFT_START + " INTEGER DEFAULT NULL, " +
                        COLUMN_NAME_LOGGED_SHIFT_END + " INTEGER DEFAULT NULL, " +
                        BASE_CHECKS + ", " +
                        SHIFT_CHECKS + ", " +
                        "CHECK (" + COLUMN_NAME_LOGGED_SHIFT_START + " <= " + COLUMN_NAME_LOGGED_SHIFT_END + ")" +
                        ")",
                SQL_CREATE_INDEX = createIndex(INDEX, false, TABLE_NAME, COLUMN_NAME_SHIFT_START),
                SQL_CREATE_TRIGGER_BEFORE_INSERT,
                SQL_CREATE_TRIGGER_BEFORE_UPDATE,
                SQL_DROP_TABLE = dropTable(TABLE_NAME);

        static {
            OVERLAPPING_SHIFT_CRITERIA = "(" + getOverlappingShiftCriteria(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END) + ") OR (" + getOverlappingShiftCriteria(COLUMN_NAME_LOGGED_SHIFT_START, COLUMN_NAME_LOGGED_SHIFT_END) + ")";
            SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true, TABLE_NAME, OVERLAPPING_SHIFT_CRITERIA);
            SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false, TABLE_NAME, OVERLAPPING_SHIFT_CRITERIA);
        }
    }

    public static class AdditionalShifts {
        public static final String
                TABLE_NAME = "additional_shifts",
                INDEX = INDEX_PREFIX + TABLE_NAME,
                SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                        BASE_COLUMN_DEFINITIONS + ", " +
                        SHIFT_COLUMN_DEFINITIONS + ", " +
                        PAYABLE_COLUMN_DEFINITIONS + ", " +
                        BASE_CHECKS + ", " +
                        SHIFT_CHECKS + ", " +
                        PAYABLE_CHECKS +
                        ")",
                SQL_CREATE_INDEX = createIndex(INDEX, false, TABLE_NAME, COLUMN_NAME_SHIFT_START),
                SQL_CREATE_TRIGGER_BEFORE_INSERT,
                SQL_CREATE_TRIGGER_BEFORE_UPDATE,
                SQL_DROP_TABLE = dropTable(TABLE_NAME);
        private static final String
                OVERLAPPING_SHIFT_CRITERIA;

        static {
            OVERLAPPING_SHIFT_CRITERIA = getOverlappingShiftCriteria(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END);
            SQL_CREATE_TRIGGER_BEFORE_INSERT = createTrigger(true, TABLE_NAME, OVERLAPPING_SHIFT_CRITERIA);
            SQL_CREATE_TRIGGER_BEFORE_UPDATE = createTrigger(false, TABLE_NAME, OVERLAPPING_SHIFT_CRITERIA);
        }
    }

    public static class CrossCoverShifts {
        public static final String
                TABLE_NAME = "cross_cover",
                INDEX = INDEX_PREFIX + TABLE_NAME,
                COLUMN_NAME_DATE = "date",
                SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                        BASE_COLUMN_DEFINITIONS + ", " +
                        PAYABLE_COLUMN_DEFINITIONS + ", " +
                        COLUMN_NAME_DATE + " INTEGER NOT NULL, " +
                        BASE_CHECKS + ", " +
                        PAYABLE_CHECKS +
                        ")",
                SQL_CREATE_INDEX = createIndex(INDEX, true, TABLE_NAME, COLUMN_NAME_DATE),
                SQL_DROP_TABLE = dropTable(TABLE_NAME);
    }

    public static class Expenses {
        public static final String
                TABLE_NAME = "expenses",
                COLUMN_NAME_TITLE = "title",
                SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                        BASE_COLUMN_DEFINITIONS + ", " +
                        PAYABLE_COLUMN_DEFINITIONS + ", " +
                        COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                        BASE_CHECKS + ", " +
                        PAYABLE_CHECKS +
                        checkHasLength(COLUMN_NAME_TITLE) +
                        ")",
                SQL_DROP_TABLE = dropTable(TABLE_NAME);
    }
}
