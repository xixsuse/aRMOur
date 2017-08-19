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
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.entity.CrossCoverEntity;
import com.skepticalone.armour.data.entity.ExpenseEntity;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.newData.NewRosteredShiftDao;
import com.skepticalone.armour.data.util.DateTimeConverter;
import com.skepticalone.armour.data.util.LocalDateConverter;
import com.skepticalone.armour.data.util.MoneyConverter;

@Database(entities = {RosteredShiftEntity.class, AdditionalShiftEntity.class, CrossCoverEntity.class, ExpenseEntity.class}, version = 1)
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
                    .addCallback(new DatabaseCallback())
                    .build();
        }
        return DATABASE;
    }

    public abstract RosteredShiftDao rosteredShiftDao();

    public abstract AdditionalShiftDao additionalShiftDao();

    public abstract CrossCoverDao crossCoverDao();

    public abstract ExpenseDao expenseDao();

    public abstract NewRosteredShiftDao newRosteredShiftDao();

}
