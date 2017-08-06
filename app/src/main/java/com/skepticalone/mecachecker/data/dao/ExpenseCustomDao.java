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

import java.util.List;

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

    @NonNull
    @Override
    String getTableName() {
        return Contract.Expenses.TABLE_NAME;
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
    public abstract LiveData<ExpenseEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract ExpenseEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.Expenses.TABLE_NAME + " ORDER BY CASE WHEN " + Contract.COLUMN_NAME_CLAIMED + " IS NULL THEN 2 WHEN " + Contract.COLUMN_NAME_PAID + " IS NULL THEN 1 ELSE 0 END, coalesce(" + Contract.COLUMN_NAME_PAID + ", " + Contract.COLUMN_NAME_CLAIMED + ", " + BaseColumns._ID + ")")
    public abstract LiveData<List<ExpenseEntity>> getItems();
}
