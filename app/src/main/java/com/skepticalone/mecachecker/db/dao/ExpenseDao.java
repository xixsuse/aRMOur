package com.skepticalone.mecachecker.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(ExpenseEntity expense);

    @Query("SELECT * FROM expenses")
    LiveData<List<ExpenseEntity>> getExpenses();

    @Query("SELECT * FROM expenses WHERE id = :id")
    LiveData<ExpenseEntity> getExpense(long id);

    @Update
    void updateExpense(ExpenseEntity expense);

    @Delete
    void deleteExpense(ExpenseEntity expense);

}