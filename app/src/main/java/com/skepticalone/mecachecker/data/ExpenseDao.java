package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.List;

@Dao
interface ExpenseDao {
    @Insert
    void insertExpense(ExpenseEntity expense);

    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME + " " +
            "ORDER BY " +
            Contract.Expenses.COLUMN_NAME_PAID +
            " IS NULL, " +
            Contract.Expenses.COLUMN_NAME_CLAIMED +
            " IS NULL, IFNULL(" +
            Contract.Expenses.COLUMN_NAME_PAID +
            ", " +
            Contract.Expenses.COLUMN_NAME_CLAIMED +
            ")")
    LiveData<List<ExpenseEntity>> getExpenses();

    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<ExpenseEntity> getExpense(long id);

    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_TITLE +
            " = :title WHERE " +
            BaseColumns._ID +
            " = :id")
    void setTitle(long id, @NonNull String title);

    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    void deleteExpense(long id);

}