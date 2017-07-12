package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

@Dao
interface ExpenseDao {

    @WorkerThread
    @Insert
    void insertExpense(ExpenseEntity expense);

    @MainThread
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

    @MainThread
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<ExpenseEntity> getExpense(long id);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_TITLE +
            " = :title WHERE " +
            BaseColumns._ID +
            " = :id")
    void setTitle(long id, @NonNull String title);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPayment(long id, @NonNull BigDecimal payment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setComment(long id, @Nullable String comment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @WorkerThread
    @Query("DELETE FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    void deleteExpense(long id);

}