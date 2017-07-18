package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.database.Cursor;
import android.util.Log;

final class HelperFactory extends FrameworkSQLiteOpenHelperFactory {

    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return super.create(SupportSQLiteOpenHelper.Configuration
                .builder(configuration.context)
                .name(configuration.name)
                .version(configuration.version)
                .callback(new Callback(configuration.callback))
                .errorHandler(configuration.errorHandler)
                .build());
    }

    private static class Callback extends SupportSQLiteOpenHelper.Callback {

        private final SupportSQLiteOpenHelper.Callback mFrameworkCallback;

        private Callback(SupportSQLiteOpenHelper.Callback frameworkCallback) {
            mFrameworkCallback = frameworkCallback;
        }

        @Override
        public void onConfigure(SupportSQLiteDatabase db) {
            mFrameworkCallback.onConfigure(db);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            mFrameworkCallback.onCreate(db);
            resetTables(db);
        }

        @Override
        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            mFrameworkCallback.onUpgrade(db, oldVersion, newVersion);
            resetTables(db);
        }

        @Override
        public void onDowngrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            mFrameworkCallback.onDowngrade(db, oldVersion, newVersion);
        }

        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            mFrameworkCallback.onOpen(db);
            explain(db, "SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE);
            explain(db, "SELECT * FROM " +
                    Contract.Expenses.TABLE_NAME + " " +
                    "ORDER BY " +
                    Contract.COLUMN_NAME_PAID +
                    " IS NULL, " +
                    Contract.COLUMN_NAME_CLAIMED +
                    " IS NULL, ifnull(" +
                    Contract.COLUMN_NAME_PAID +
                    ", " +
                    Contract.COLUMN_NAME_CLAIMED +
                    ")");
        }

        private void explain(SupportSQLiteDatabase db, String query) {
            String TAG = "explain";
            Log.i(TAG, "query: " + query);
            Cursor c = db.query("EXPLAIN QUERY PLAN " + query);
            int columnCount = c.getColumnCount();
            while (c.moveToNext()) {
                Log.i(TAG, "---row " + c.getPosition());
                for (String columnName : c.getColumnNames()) {
                    Log.i(TAG, "------" + columnName + ": " + c.getString(c.getColumnIndex(columnName)));
                }
            }
            c.close();

        }

        private void resetTables(SupportSQLiteDatabase db) {
            db.execSQL(Contract.RosteredShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.AdditionalShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.CrossCoverShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.Expenses.SQL_DROP_TABLE);
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_TABLE);
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_INDEX);
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TABLE);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_INDEX);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
            db.execSQL(Contract.CrossCoverShifts.SQL_CREATE_TABLE);
            db.execSQL(Contract.CrossCoverShifts.SQL_CREATE_INDEX);
            db.execSQL(Contract.Expenses.SQL_CREATE_TABLE);
        }

    }
}
