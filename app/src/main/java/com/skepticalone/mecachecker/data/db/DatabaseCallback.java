package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

final class DatabaseCallback extends RoomDatabase.Callback {

    private static final String TAG = "DatabaseCallback";

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
    }

    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        Cursor cursor = db.query("SELECT sql FROM sqlite_master ORDER BY name");
        while (cursor.moveToNext()) {
            Log.i(TAG, cursor.getString(0));
        }
        cursor.close();
    }
}
