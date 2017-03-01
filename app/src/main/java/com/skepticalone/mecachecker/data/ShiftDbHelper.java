package com.skepticalone.mecachecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class ShiftDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "Shifts.db";

    ShiftDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShiftContract.Shift.SQL_CREATE_ENTRIES);
        db.execSQL(ShiftContract.Shift.SQL_CREATE_START_INDEX);
        db.execSQL(ShiftContract.Shift.SQL_CREATE_CATEGORY_INDEX);
        db.execSQL(ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ShiftContract.Shift.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
