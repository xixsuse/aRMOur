package com.skepticalone.mecachecker.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

import org.joda.time.DateTime;

import java.util.List;

import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_CLAIMED;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_PAID;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_TITLE;
import static com.skepticalone.mecachecker.db.Contract.Expenses.TABLE_NAME;
import static com.skepticalone.mecachecker.db.Contract.Expenses._ID;

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(ExpenseEntity expense);

    @Query("SELECT * FROM " +
            TABLE_NAME + " " +
            "ORDER BY " +
            COLUMN_NAME_PAID +
            " IS NULL, " +
            COLUMN_NAME_CLAIMED +
            " IS NULL, IFNULL(" +
            COLUMN_NAME_PAID +
            ", " +
            COLUMN_NAME_CLAIMED +
            ")")
    LiveData<List<ExpenseEntity>> getExpenses();

    @Query("SELECT * FROM " +
            TABLE_NAME +
            " WHERE " +
            _ID +
            " = :id")
    LiveData<ExpenseEntity> getExpense(long id);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_TITLE +
            " = :title WHERE " +
            _ID +
            " = :id")
    void setTitle(long id, @NonNull String title);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            _ID +
            " = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_PAID +
            " = :paid WHERE " +
            _ID +
            " = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM " +
            TABLE_NAME +
            " WHERE " +
            _ID +
            " = :id")
    void deleteExpense(long id);

}