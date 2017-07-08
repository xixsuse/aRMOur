package com.skepticalone.mecachecker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.skepticalone.mecachecker.db.converter.DateTimeConverter;
import com.skepticalone.mecachecker.db.converter.LocalDateConverter;
import com.skepticalone.mecachecker.db.converter.MoneyConverter;
import com.skepticalone.mecachecker.db.dao.CrossCoverDao;
import com.skepticalone.mecachecker.db.dao.ExpenseDao;
import com.skepticalone.mecachecker.db.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

@Database(entities = {CrossCoverEntity.class, ExpenseEntity.class}, version = 4)
@TypeConverters({LocalDateConverter.class, DateTimeConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";

    private static AppDatabase DATABASE;

    public static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    // TODO: 21/06/17 remove this
                    .allowMainThreadQueries()
                    .openHelperFactory(new HelperFactory())
                    .build();
        }
        return DATABASE;
    }

    public abstract CrossCoverDao crossCoverDao();
    public abstract ExpenseDao expenseDao();

}
