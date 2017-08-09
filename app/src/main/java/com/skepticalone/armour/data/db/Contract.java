package com.skepticalone.armour.data.db;

import android.provider.BaseColumns;

public final class Contract {
    public static final String
            COLUMN_NAME_PAYMENT = "payment",
            COLUMN_NAME_CLAIMED = "claimed",
            COLUMN_NAME_PAID = "paid",
            COLUMN_NAME_COMMENT = "comment",
            COLUMN_NAME_SHIFT_START = "shift_start",
            COLUMN_NAME_SHIFT_END = "shift_end";

    private Contract() {
    }

    private static String getOverlappingShiftCriteria(String columnNameStart, String columnNameEnd) {
        return columnNameStart + " < NEW." + columnNameEnd + " AND NEW." + columnNameStart + " < " + columnNameEnd;
    }

    private static String getUpdateEventString(String... columns) {
        StringBuilder sb = new StringBuilder("UPDATE OF");
        for (int i = 0; i < columns.length ; i++) {
            sb.append(i == 0 ? " " : ", ").append(columns[i]);
        }
        return sb.toString();
    }

    private static String createTrigger(String triggerSuffix, String tableName, String changeEvent, String abortCriteria) {
        return "CREATE TRIGGER trigger_" + tableName + triggerSuffix + " BEFORE " + changeEvent + " ON " + tableName + " BEGIN SELECT CASE WHEN EXISTS (SELECT 1 FROM " + tableName + " WHERE " + abortCriteria + ") THEN RAISE (ABORT, 'Overlap: " + tableName + triggerSuffix + "') END; END";
    }

    private static String createUpdateTrigger(String tableName, String updateEventString, String overlappingShiftCriteria) {
        return createTrigger(
                "_update",
                tableName,
                updateEventString,
                BaseColumns._ID + " != NEW." + BaseColumns._ID + " AND (" + overlappingShiftCriteria + ")"
        );
    }

    private static String createInsertTrigger(String tableName, String overlappingShiftCriteria) {
        return createTrigger(
                "_insert",
                tableName,
                "INSERT",
                overlappingShiftCriteria
        );
    }

    public static class RosteredShifts {
        public static final String
                TABLE_NAME = "rostered_shifts",
                LOGGED_PREFIX = "logged_",
                COLUMN_NAME_LOGGED_SHIFT_START = LOGGED_PREFIX + COLUMN_NAME_SHIFT_START,
                COLUMN_NAME_LOGGED_SHIFT_END = LOGGED_PREFIX + COLUMN_NAME_SHIFT_END;
        static final String
                SQL_CREATE_TRIGGER_BEFORE_INSERT,
                SQL_CREATE_TRIGGER_BEFORE_UPDATE;
        private static final String OVERLAPPING_SHIFT_CRITERIA;
        static {
            OVERLAPPING_SHIFT_CRITERIA = "(" + getOverlappingShiftCriteria(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END) + ") OR (" + getOverlappingShiftCriteria(COLUMN_NAME_LOGGED_SHIFT_START, COLUMN_NAME_LOGGED_SHIFT_END) + ")";
            SQL_CREATE_TRIGGER_BEFORE_INSERT = createInsertTrigger(
                    TABLE_NAME,
                    OVERLAPPING_SHIFT_CRITERIA
            );
            SQL_CREATE_TRIGGER_BEFORE_UPDATE = createUpdateTrigger(
                    TABLE_NAME,
                    getUpdateEventString(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END, COLUMN_NAME_LOGGED_SHIFT_START, COLUMN_NAME_LOGGED_SHIFT_END),
                    OVERLAPPING_SHIFT_CRITERIA
            );
        }
    }

    public static class AdditionalShifts {
        public static final String
                TABLE_NAME = "additional_shifts";
        static final String
                SQL_CREATE_TRIGGER_BEFORE_INSERT,
                SQL_CREATE_TRIGGER_BEFORE_UPDATE;
        private static final String OVERLAPPING_SHIFT_CRITERIA;
        static {
            OVERLAPPING_SHIFT_CRITERIA = getOverlappingShiftCriteria(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END);
            SQL_CREATE_TRIGGER_BEFORE_INSERT = createInsertTrigger(
                    TABLE_NAME,
                    OVERLAPPING_SHIFT_CRITERIA
            );
            SQL_CREATE_TRIGGER_BEFORE_UPDATE = createUpdateTrigger(
                    TABLE_NAME,
                    getUpdateEventString(COLUMN_NAME_SHIFT_START, COLUMN_NAME_SHIFT_END),
                    OVERLAPPING_SHIFT_CRITERIA
            );
        }
    }

    public static class CrossCoverShifts {
        public static final String
                TABLE_NAME = "cross_cover",
                COLUMN_NAME_DATE = "date";
    }

    public static class Expenses {
        public static final String
                TABLE_NAME = "expenses",
                COLUMN_NAME_TITLE = "title";
    }
}
