package com.skepticalone.armour.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.dao.RawAdditionalShiftDao;
import com.skepticalone.armour.data.dao.CrossCoverDao;
import com.skepticalone.armour.data.dao.ExpenseDao;
import com.skepticalone.armour.data.dao.RawRosteredShiftDao;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.model.RawExpenseEntity;
import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.util.InstantConverter;
import com.skepticalone.armour.data.util.LocalDateConverter;
import com.skepticalone.armour.data.util.MoneyConverter;

@Database(entities = {RawRosteredShiftEntity.class, RawAdditionalShiftEntity.class, RawCrossCoverEntity.class, RawExpenseEntity.class}, version = 3)
@TypeConverters({LocalDateConverter.class, InstantConverter.class, MoneyConverter.class})
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
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(SupportSQLiteDatabase database) {
                        }
                    }, new Migration2to3())
                    .build();
        }
        return DATABASE;
    }

    public abstract RawRosteredShiftDao rosteredShiftDao();

    public abstract RawAdditionalShiftDao additionalShiftDao();

    public abstract CrossCoverDao crossCoverDao();

    public abstract ExpenseDao expenseDao();

}
