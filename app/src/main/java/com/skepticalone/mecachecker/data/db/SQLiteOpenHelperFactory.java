package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;

final class SQLiteOpenHelperFactory extends FrameworkSQLiteOpenHelperFactory {

    @Override
    public final SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return super.create(SupportSQLiteOpenHelper.Configuration
                .builder(configuration.context)
                .name(configuration.name)
                .version(configuration.version)
                .callback(new OpenHelper(configuration.callback))
                .errorHandler(configuration.errorHandler)
                .build());
    }

    private final static class OpenHelper extends SupportSQLiteOpenHelper.Callback {

        private final SupportSQLiteOpenHelper.Callback mFrameworkCallback;

        private OpenHelper(SupportSQLiteOpenHelper.Callback frameworkCallback) {
            mFrameworkCallback = frameworkCallback;
        }

        @Override
        public void onConfigure(SupportSQLiteDatabase db) {
            mFrameworkCallback.onConfigure(db);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            mFrameworkCallback.onCreate(db);
            createTriggers(db);
        }

        @Override
        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            mFrameworkCallback.onUpgrade(db, oldVersion, newVersion);
            createTriggers(db);
        }

        @Override
        public void onDowngrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            mFrameworkCallback.onDowngrade(db, oldVersion, newVersion);
            createTriggers(db);
        }

        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            mFrameworkCallback.onOpen(db);
//            explain(db, "SELECT sql FROM sqlite_master ORDER BY name");
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT * FROM " + Contract.RosteredShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
//                    Contract.RosteredShifts.TABLE_NAME +
//                    " ORDER BY " +
//                    Contract.COLUMN_NAME_SHIFT_END +
//                    " DESC LIMIT 1"
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
//                    Contract.AdditionalShifts.TABLE_NAME +
//                    " ORDER BY " +
//                    Contract.COLUMN_NAME_SHIFT_END +
//                    " DESC LIMIT 1"
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT " + Contract.CrossCoverShifts.COLUMN_NAME_DATE + " FROM " +
//                    Contract.CrossCoverShifts.TABLE_NAME +
//                    " ORDER BY " +
//                    Contract.CrossCoverShifts.COLUMN_NAME_DATE +
//                    " DESC LIMIT 1"
//            );
//            explain(db, "EXPLAIN QUERY PLAN " +
//                    "SELECT * FROM " + Contract.Expenses.TABLE_NAME + " ORDER BY CASE WHEN " + Contract.COLUMN_NAME_CLAIMED + " IS NULL THEN 2 WHEN " + Contract.COLUMN_NAME_PAID + " IS NULL THEN 1 ELSE 0 END, coalesce(" + Contract.COLUMN_NAME_PAID + ", " + Contract.COLUMN_NAME_CLAIMED + ", " + BaseColumns._ID + ")"
//            );
        }
//
//        private void explain(SupportSQLiteDatabase db, String query) {
//            String TAG = "explain";
//            Log.i(TAG, "query: " + query);
//            Cursor c = db.query(query);
//            int columnCount = c.getColumnCount();
//            while (c.moveToNext()) {
//                Log.i(TAG, "---row " + c.getPosition());
//                for (String columnName : c.getColumnNames()) {
//                    Log.i(TAG, "------" + columnName + ": " + c.getString(c.getColumnIndex(columnName)));
//                }
//            }
//            c.close();
//        }

        private void createTriggers(SupportSQLiteDatabase db) {
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
            db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
            db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        }

    }
}
