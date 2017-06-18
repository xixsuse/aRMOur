package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(Expense expense);

    @Query("SELECT * FROM EXPENSE")
    LiveData<List<Expense>> getExpenses();

    @Query("SELECT * FROM EXPENSE WHERE id = :id LIMIT 1")
    LiveData<Expense> getExpense(long id);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);
}