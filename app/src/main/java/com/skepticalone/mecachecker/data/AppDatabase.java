package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Expense.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "sample-db";

    public abstract ExpenseDao expenseDao();
}
