package com.skepticalone.armour.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

final class Migration2to3 extends Migration {

    Migration2to3() {
        super(2, 3);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.beginTransaction();
        try {

            String[] columns;
            Cursor cursor;
            String whereClause = BaseColumns._ID + "=?";
            ContentValues values = new ContentValues();
            Long[] whereArgs;
            ZoneId zoneId = ZoneId.systemDefault();

            columns = new String[]{
                    BaseColumns._ID,
                    Contract.COLUMN_NAME_CLAIMED,
                    Contract.COLUMN_NAME_PAID
            };
            cursor = database.query(SupportSQLiteQueryBuilder.builder(Contract.Expenses.TABLE_NAME)
                    .columns(columns)
                    .create());
            while (cursor.moveToNext()) {
                values.clear();
                whereArgs = new Long[]{cursor.getLong(0)};
                if (!cursor.isNull(1)) {
                    values.put(Contract.COLUMN_NAME_CLAIMED, Instant.ofEpochMilli(cursor.getLong(2)).getEpochSecond());
                }
                if (!cursor.isNull(2)) {
                    values.put(Contract.COLUMN_NAME_PAID, Instant.ofEpochMilli(cursor.getLong(3)).getEpochSecond());
                }

                database.update(Contract.Expenses.TABLE_NAME, SQLiteDatabase.CONFLICT_ROLLBACK, values, whereClause, whereArgs);
            }
            cursor.close();

            columns = new String[]{
                    BaseColumns._ID,
                    Contract.CrossCoverShifts.COLUMN_NAME_DATE,
                    Contract.COLUMN_NAME_CLAIMED,
                    Contract.COLUMN_NAME_PAID
            };
            cursor = database.query(SupportSQLiteQueryBuilder.builder(Contract.CrossCoverShifts.TABLE_NAME)
                    .columns(columns)
                    .orderBy(Contract.CrossCoverShifts.COLUMN_NAME_DATE)
                    .create());
            while (cursor.moveToNext()) {
                values.clear();
                whereArgs = new Long[]{cursor.getLong(0)};
                values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, Instant.ofEpochMilli(cursor.getLong(1)).atZone(zoneId).toLocalDate().toEpochDay());
                if (!cursor.isNull(2)) {
                    values.put(Contract.COLUMN_NAME_CLAIMED, Instant.ofEpochMilli(cursor.getLong(2)).getEpochSecond());
                }
                if (!cursor.isNull(3)) {
                    values.put(Contract.COLUMN_NAME_PAID, Instant.ofEpochMilli(cursor.getLong(3)).getEpochSecond());
                }
                database.update(Contract.CrossCoverShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ROLLBACK, values, whereClause, whereArgs);
            }
            cursor.close();

            columns = new String[]{
                    BaseColumns._ID,
                    Contract.COLUMN_NAME_SHIFT_START,
                    Contract.COLUMN_NAME_SHIFT_END,
                    Contract.COLUMN_NAME_CLAIMED,
                    Contract.COLUMN_NAME_PAID
            };
            cursor = database.query(SupportSQLiteQueryBuilder.builder(Contract.AdditionalShifts.TABLE_NAME)
                    .columns(columns)
                    .orderBy(Contract.COLUMN_NAME_SHIFT_START)
                    .create());
            while (cursor.moveToNext()) {
                values.clear();
                whereArgs = new Long[]{cursor.getLong(0)};
                values.put(Contract.COLUMN_NAME_SHIFT_START, Instant.ofEpochMilli(cursor.getLong(1)).getEpochSecond());
                values.put(Contract.COLUMN_NAME_SHIFT_END, Instant.ofEpochMilli(cursor.getLong(2)).getEpochSecond());
                if (!cursor.isNull(3)) {
                    values.put(Contract.COLUMN_NAME_CLAIMED, Instant.ofEpochMilli(cursor.getLong(3)).getEpochSecond());
                }
                if (!cursor.isNull(4)) {
                    values.put(Contract.COLUMN_NAME_PAID, Instant.ofEpochMilli(cursor.getLong(4)).getEpochSecond());
                }
                database.update(Contract.AdditionalShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ROLLBACK, values, whereClause, whereArgs);
            }
            cursor.close();

            columns = new String[]{
                    BaseColumns._ID,
                    Contract.COLUMN_NAME_SHIFT_START,
                    Contract.COLUMN_NAME_SHIFT_END,
                    Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START,
                    Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END
            };
            cursor = database.query(SupportSQLiteQueryBuilder.builder(Contract.RosteredShifts.TABLE_NAME)
                    .columns(columns)
                    .orderBy(Contract.COLUMN_NAME_SHIFT_START)
                    .create());
            while (cursor.moveToNext()) {
                values.clear();
                whereArgs = new Long[]{cursor.getLong(0)};
                values.put(Contract.COLUMN_NAME_SHIFT_START, Instant.ofEpochMilli(cursor.getLong(1)).getEpochSecond());
                values.put(Contract.COLUMN_NAME_SHIFT_END, Instant.ofEpochMilli(cursor.getLong(2)).getEpochSecond());
                if (!cursor.isNull(3)) {
                    values.put(Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START, Instant.ofEpochMilli(cursor.getLong(3)).getEpochSecond());
                }
                if (!cursor.isNull(4)) {
                    values.put(Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END, Instant.ofEpochMilli(cursor.getLong(4)).getEpochSecond());
                }
                database.update(Contract.RosteredShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ROLLBACK, values, whereClause, whereArgs);
            }
            cursor.close();

            database.setTransactionSuccessful();

        } finally {
            database.endTransaction();
        }
    }
}
