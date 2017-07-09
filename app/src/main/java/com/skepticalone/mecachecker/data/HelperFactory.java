package com.skepticalone.mecachecker.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;

class HelperFactory extends FrameworkSQLiteOpenHelperFactory {

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
        }

        private void resetTables(SupportSQLiteDatabase db) {
            db.execSQL(Contract.CrossCoverShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.Expenses.SQL_DROP_TABLE);
            db.execSQL(Contract.CrossCoverShifts.SQL_CREATE_TABLE);
            db.execSQL(Contract.Expenses.SQL_CREATE_TABLE);
        }

    }
}
