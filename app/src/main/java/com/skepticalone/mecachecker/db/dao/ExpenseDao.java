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

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(ExpenseEntity expense);

    @Query("SELECT * FROM expenses ORDER BY paid IS NULL, claimed IS NULL, IFNULL(paid, claimed)")
    LiveData<List<ExpenseEntity>> getExpenses();

    @Query("SELECT * FROM expenses WHERE id = :id")
    LiveData<ExpenseEntity> getExpense(long id);

    @Query("UPDATE expenses SET title = :title WHERE id = :id")
    void setTitle(long id, @NonNull String title);

    @Query("UPDATE expenses SET claimed = :claimed WHERE id = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE expenses SET paid = :paid WHERE id = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM expenses WHERE id = :id")
    void deleteExpense(long id);

}