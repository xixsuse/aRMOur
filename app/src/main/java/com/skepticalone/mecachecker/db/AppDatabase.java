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

@Database(entities = {CrossCoverEntity.class, ExpenseEntity.class}, version = 1)
@TypeConverters({LocalDateConverter.class, DateTimeConverter.class, MoneyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";
    private static AppDatabase DATABASE;

    public static AppDatabase getInstance(Context applicationContext) {
        if (DATABASE == null) {
//            applicationContext.deleteDatabase(DATABASE_NAME);
            DATABASE = Room
                    .databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME)
                    // TODO: 21/06/17 remove this
                    .allowMainThreadQueries()
//                    .openHelperFactory(new FrameworkSQLiteOpenHelperFactory(){
//                        @Override
//                        public SupportSQLiteOpenHelper create(final SupportSQLiteOpenHelper.Configuration configuration) {
//                            return super.create(SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
//                                    .callback(new SupportSQLiteOpenHelper.Callback() {
//                                        @Override
//                                        public void onConfigure(SupportSQLiteDatabase db) {
//                                            configuration.callback.onConfigure(db);
//                                        }
//
//                                        @Override
//                                        public void onOpen(SupportSQLiteDatabase db) {
//                                            configuration.callback.onOpen(db);
//                                        }
//
//                                        @Override
//                                        public void onCreate(SupportSQLiteDatabase db) {
//                                            db.execSQL(CrossCoverEntity.SQL_CREATE_TABLE);
//                                            db.execSQL(ExpenseEntity.SQL_CREATE_TABLE);
//                                        }
//
//                                        @Override
//                                        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
//                                            db.execSQL(CrossCoverEntity.SQL_DROP_TABLE);
//                                            db.execSQL(ExpenseEntity.SQL_DROP_TABLE);
//                                            onCreate(db);
//                                        }
//                                    })
//                                    .errorHandler(configuration.errorHandler)
//                                    .name(configuration.name)
//                                    .version(configuration.version)
//                                    .build()
//                            );
//                        }
//                    })
                    .build();
//            DatabaseInitUtil.initializeDb(DATABASE);
        }
        return DATABASE;
    }

    public abstract CrossCoverDao crossCoverDao();
    public abstract ExpenseDao expenseDao();

}
