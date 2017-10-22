package com.skepticalone.armour.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

final class Migration1to2 extends Migration {

    @NonNull
    private final ZoneId timezone;

    Migration1to2(@NonNull Context context) {
        super(1, 2);
        String zoneId = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_time_zone_id), null);
        timezone = zoneId == null ? ZoneId.systemDefault() : ZoneId.of(zoneId);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.beginTransaction();
        try {
            String[] columns;
            Cursor cursor;
            String whereClause = BaseColumns._ID + "=?";
            ContentValues values = new ContentValues();
            Long[] whereArgs;

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
                if (values.size() != 0) {
                    database.update(Contract.Expenses.TABLE_NAME, SQLiteDatabase.CONFLICT_ROLLBACK, values, whereClause, whereArgs);
                }
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
                values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, Instant.ofEpochMilli(cursor.getLong(1)).atZone(timezone).toLocalDate().toEpochDay());
                if (!cursor.isNull(2)) {
                    values.put(Contract.COLUMN_NAME_CLAIMED, Instant.ofEpochMilli(cursor.getLong(2)).getEpochSecond());
                }
                if (!cursor.isNull(3)) {
                    values.put(Contract.COLUMN_NAME_PAID, Instant.ofEpochMilli(cursor.getLong(3)).getEpochSecond());
                }
                database.update(Contract.CrossCoverShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, whereClause, whereArgs);
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
