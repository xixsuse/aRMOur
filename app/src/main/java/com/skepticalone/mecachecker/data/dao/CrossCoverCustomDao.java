package com.skepticalone.mecachecker.data.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.LocalDateConverter;

import org.joda.time.LocalDate;

@Dao
public abstract class CrossCoverCustomDao extends CustomDao {

    @NonNull
    private final SupportSQLiteStatement insertStatement;

    @NonNull
    private static final String GET_LAST_SHIFT_DATE =
            "SELECT " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " DESC LIMIT 1";

    CrossCoverCustomDao(@NonNull AppDatabase database) {
        super(database);
        insertStatement = getDatabase().compileStatement("INSERT INTO " + Contract.CrossCoverShifts.TABLE_NAME + " (" +
                Contract.COLUMN_NAME_PAYMENT +
                ", " +
                Contract.CrossCoverShifts.COLUMN_NAME_DATE +
                ") VALUES (?,?)");
    }

    synchronized public final long insertSync(int paymentInCents){
        insertStatement.bindLong(1, paymentInCents);
        Cursor cursor = getDatabase().query(GET_LAST_SHIFT_DATE, null);
        @Nullable final LocalDate lastDate = cursor.moveToFirst() ? LocalDateConverter.millisToDate(cursor.getLong(0)) : null;
        cursor.close();
        insertStatement.bindLong(2, LocalDateConverter.dateToMillis(CrossCoverEntity.getNewDate(lastDate)));
        getDatabase().beginTransaction();
        try {
            long id = insertStatement.executeInsert();
            getDatabase().setTransactionSuccessful();
            return id;
        } finally {
            getDatabase().endTransaction();
        }
    }

}
