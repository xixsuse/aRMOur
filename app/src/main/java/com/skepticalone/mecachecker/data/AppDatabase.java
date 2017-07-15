package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {CrossCoverEntity.class, ExpenseEntity.class}, version = 13)
@TypeConverters({LocalDateConverter.class, DateTimeConverter.class, MoneyConverter.class})
abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";

    private static AppDatabase DATABASE;

    static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    .openHelperFactory(new HelperFactory())
                    .build();
        }
        return DATABASE;
    }

    abstract CrossCoverDao crossCoverDao();

    abstract ExpenseDao expenseDao();

}
