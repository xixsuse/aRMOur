package com.skepticalone.mecachecker.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
import com.skepticalone.mecachecker.data.dao.CrossCoverDao;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.dao.RosteredShiftDao;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.DateTimeConverter;
import com.skepticalone.mecachecker.data.util.LocalDateConverter;
import com.skepticalone.mecachecker.data.util.MoneyConverter;

@Database(entities = {RosteredShiftEntity.class, AdditionalShiftEntity.class, CrossCoverEntity.class, ExpenseEntity.class},
        version = 28)
@TypeConverters({LocalDateConverter.class, DateTimeConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";

    @Nullable
    private static AppDatabase DATABASE;

    @NonNull
    public static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    .openHelperFactory(new HelperFactory())
                    .build();
        }
        return DATABASE;
    }

    public abstract RosteredShiftDao rosteredShiftDao();

    public abstract AdditionalShiftDao additionalShiftDao();

    public abstract CrossCoverDao crossCoverDao();

    public abstract ExpenseDao expenseDao();

}
