package com.skepticalone.armour.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.dao.AdditionalShiftDao;
import com.skepticalone.armour.data.dao.CrossCoverDao;
import com.skepticalone.armour.data.dao.ExpenseDao;
import com.skepticalone.armour.data.dao.RosteredShiftDao;
import com.skepticalone.armour.data.model.AdditionalShiftEntity;
import com.skepticalone.armour.data.model.CrossCoverEntity;
import com.skepticalone.armour.data.model.ExpenseEntity;
import com.skepticalone.armour.data.model.RosteredShiftEntity;
import com.skepticalone.armour.util.InstantConverter;
import com.skepticalone.armour.util.LocalDateConverter;
import com.skepticalone.armour.util.MoneyConverter;

@Database(entities = {RosteredShiftEntity.class, AdditionalShiftEntity.class, CrossCoverEntity.class, ExpenseEntity.class}, version = 2)
@TypeConverters({LocalDateConverter.class, InstantConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";

    @Nullable
    private static AppDatabase DATABASE;

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (DATABASE == null) {
            synchronized (AppDatabase.class) {
                if (DATABASE == null) {
                    DATABASE = Room
                            .databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                            .addCallback(new DatabaseCallback())
                            .addMigrations(new Migration1to2(context))
                            .build();
                }
            }
        }
        return DATABASE;
    }

    public abstract RosteredShiftDao rosteredShiftDao();

    public abstract AdditionalShiftDao additionalShiftDao();

    public abstract CrossCoverDao crossCoverDao();

    public abstract ExpenseDao expenseDao();

}
