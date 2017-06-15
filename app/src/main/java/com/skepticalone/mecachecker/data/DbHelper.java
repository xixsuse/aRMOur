package com.skepticalone.mecachecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 19;
    private static final String DATABASE_NAME = "database.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TABLE);
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_INDEX_START);
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TABLE);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_INDEX_START);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(Contract.CrossCoverShifts.SQL_CREATE_TABLE);
        db.execSQL(Contract.Expenses.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.RosteredShifts.SQL_DROP_TABLE);
        db.execSQL(Contract.AdditionalShifts.SQL_DROP_TABLE);
        db.execSQL(Contract.CrossCoverShifts.SQL_DROP_TABLE);
        db.execSQL(Contract.Expenses.SQL_DROP_TABLE);
        onCreate(db);
    }
}
