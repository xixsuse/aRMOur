package com.skepticalone.mecachecker.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.util.Log;

public class MyFactory implements SupportSQLiteOpenHelper.Factory {

    private final FrameworkSQLiteOpenHelperFactory mFrameworkSQLiteOpenHelperFactory;

    MyFactory() {
        mFrameworkSQLiteOpenHelperFactory = new FrameworkSQLiteOpenHelperFactory();
    }

    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return mFrameworkSQLiteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration
                .builder(configuration.context)
                .name(configuration.name)
                .version(configuration.version)
                .callback(new MyCallback())
                .errorHandler(configuration.errorHandler)
                .build());
    }

    static class MyCallback extends SupportSQLiteOpenHelper.Callback {
        private static final String TAG = "MyCallback";

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            Log.d(TAG, "onCreate() called with: db = [" + db + "]");
//        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TABLE);
//        db.execSQL(Contract.RosteredShifts.SQL_CREATE_INDEX_START);
//        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
//        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
//        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TABLE);
//        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_INDEX_START);
//        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
//        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
            db.execSQL(Contract.CrossCoverShifts.SQL_CREATE_TABLE);
            db.execSQL(Contract.Expenses.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpgrade() called with: db = [" + db + "], oldVersion = [" + oldVersion + "], newVersion = [" + newVersion + "]");
//        db.execSQL(Contract.RosteredShifts.SQL_DROP_TABLE);
//        db.execSQL(Contract.AdditionalShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.CrossCoverShifts.SQL_DROP_TABLE);
            db.execSQL(Contract.Expenses.SQL_DROP_TABLE);
            onCreate(db);
        }

    }

}
