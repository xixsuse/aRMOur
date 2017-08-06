package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.LocalDateConverter;

import org.joda.time.LocalDate;

import java.util.List;

@Dao
public abstract class CrossCoverCustomDao extends CustomDao<CrossCoverEntity> {

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

    @NonNull
    @Override
    String getTableName() {
        return Contract.CrossCoverShifts.TABLE_NAME;
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

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<CrossCoverEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract CrossCoverEntity getItemSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    public abstract LiveData<List<CrossCoverEntity>> getItems();

}
