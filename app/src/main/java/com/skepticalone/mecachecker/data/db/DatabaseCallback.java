package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

final class DatabaseCallback extends RoomDatabase.Callback {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
    }
}
