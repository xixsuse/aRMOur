package com.skepticalone.armour.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Migration1to2Test {

    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AppDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrate() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 1);
        new DatabaseCallback().onCreate(db);
        ContentValues values = new ContentValues();
        values.put(Contract.Expenses.COLUMN_NAME_TITLE, "Random 1");
        values.put(Contract.COLUMN_NAME_PAYMENT, 400);
        db.insert(Contract.Expenses.TABLE_NAME, SQLiteDatabase.CONFLICT_ABORT, values);
        values.clear();
        LocalDate today = LocalDate.now(), tomorrow = today.plusDays(1);
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        values.put(Contract.COLUMN_NAME_PAYMENT, 100);
        db.insert(Contract.CrossCoverShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ABORT, values);
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, today.atStartOfDay(ZoneId.systemDefault()).plusHours(3).toInstant().toEpochMilli());
        values.put(Contract.COLUMN_NAME_PAYMENT, 200);
        db.insert(Contract.CrossCoverShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ABORT, values);
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        values.put(Contract.COLUMN_NAME_PAYMENT, 300);
        db.insert(Contract.CrossCoverShifts.TABLE_NAME, SQLiteDatabase.CONFLICT_ABORT, values);
        db.close();
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, new Migration1to2(InstrumentationRegistry.getTargetContext()));
        Cursor cursor;

        cursor = db.query("SELECT " + Contract.Expenses.COLUMN_NAME_TITLE + " FROM " + Contract.Expenses.TABLE_NAME);
        try {
            assertEquals(1, cursor.getCount());
            assertTrue(cursor.moveToFirst());
            assertEquals("Random 1", cursor.getString(0));
        } finally {
            cursor.close();
        }

        cursor = db.query("SELECT " + Contract.CrossCoverShifts.COLUMN_NAME_DATE + ", " + Contract.COLUMN_NAME_PAYMENT + " FROM " + Contract.CrossCoverShifts.TABLE_NAME);
        try {
            assertEquals(2, cursor.getCount());
            assertTrue(cursor.moveToFirst());
            assertEquals(today, LocalDate.ofEpochDay(cursor.getLong(0)));
            assertEquals(200, cursor.getInt(1));
            assertTrue(cursor.moveToNext());
            assertEquals(tomorrow, LocalDate.ofEpochDay(cursor.getLong(0)));
            assertEquals(300, cursor.getInt(1));
        } finally {
            cursor.close();
        }

    }

}