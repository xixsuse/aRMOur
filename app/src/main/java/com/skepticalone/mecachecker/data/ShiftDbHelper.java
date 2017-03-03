package com.skepticalone.mecachecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class ShiftDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "shifts.db";

    ShiftDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShiftContract.RosteredShift.SQL_CREATE_TABLE);
        db.execSQL(ShiftContract.RosteredShift.SQL_CREATE_INDEX_START);
        db.execSQL(ShiftContract.RosteredShift.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(ShiftContract.RosteredShift.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ShiftContract.RosteredShift.SQL_DROP_TABLE);
        onCreate(db);
    }
}
