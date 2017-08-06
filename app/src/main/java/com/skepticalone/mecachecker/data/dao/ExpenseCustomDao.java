package com.skepticalone.mecachecker.data.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;

@Dao
public abstract class ExpenseCustomDao extends CustomDao {


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

}
