package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.DateTimeConverter;
import com.skepticalone.mecachecker.data.util.LocalDateConverter;
import com.skepticalone.mecachecker.data.util.MoneyConverter;

@Database(entities = {
//        RosteredShiftEntity.class, AdditionalShiftEntity.class, CrossCoverEntity.class,
        ExpenseEntity.class}, version = 25)
@TypeConverters({LocalDateConverter.class, DateTimeConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";

    private static AppDatabase DATABASE;

    public static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    .openHelperFactory(new HelperFactory())
                    // TODO: 24/07/17
                    .allowMainThreadQueries()
                    .build();
        }
        return DATABASE;
    }

//    public abstract RosteredShiftDao rosteredShiftDao();
//
//    public abstract AdditionalShiftDao additionalShiftDao();
//
//    public abstract CrossCoverDao crossCoverDao();

    public abstract ExpenseDao expenseDao();

}
