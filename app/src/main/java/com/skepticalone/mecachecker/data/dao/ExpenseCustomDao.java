package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

@Dao
public abstract class ExpenseCustomDao extends CustomDao<ExpenseEntity> {


    @NonNull
    private final SupportSQLiteStatement insertStatement;

    ExpenseCustomDao(@NonNull AppDatabase database) {
        super(database);
        insertStatement = getDatabase().compileStatement("INSERT INTO " + Contract.Expenses.TABLE_NAME + " (" +
                Contract.COLUMN_NAME_PAYMENT +
                ", " +
                Contract.Expenses.COLUMN_NAME_TITLE +
                ") VALUES (0,?)");
    }

    synchronized public final long insertSync(@NonNull String title){
        insertStatement.bindString(1, title);
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
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract LiveData<ExpenseEntity> getItem(long id);

}
