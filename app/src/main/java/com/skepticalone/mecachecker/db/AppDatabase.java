package com.skepticalone.mecachecker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.skepticalone.mecachecker.db.converter.DateTimeConverter;
import com.skepticalone.mecachecker.db.converter.MoneyConverter;
import com.skepticalone.mecachecker.db.dao.ExpenseDao;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

@Database(entities = {ExpenseEntity.class}, version = 21)
@TypeConverters({DateTimeConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database.db";
    private static AppDatabase DATABASE;

    public static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
//            applicationContext.deleteDatabase(DATABASE_NAME);
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    // TODO: 21/06/17 remove this
                    .allowMainThreadQueries()
                    .build();
//            DatabaseInitUtil.initializeDb(DATABASE);
        }
        return DATABASE;
    }

    public abstract ExpenseDao expenseDao();

}
