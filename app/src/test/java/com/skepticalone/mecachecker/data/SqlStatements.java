package com.skepticalone.mecachecker.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SqlStatements {

    @Test
    public void createEntries() {
        assertEquals(
                ShiftContract.Shift.SQL_CREATE_ENTRIES,
                "CREATE TABLE shifts (_id INTEGER PRIMARY KEY, start INTEGER NOT NULL, end INTEGER NOT NULL, CHECK (start < end))"
        );
    }

    @Test
    public void triggerBeforeInsert() {
        assertEquals(
                ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_INSERT,
                "CREATE TRIGGER insert_trigger BEFORE INSERT ON shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM shifts WHERE NOT (end <= NEW.start OR start >= NEW.end)) THEN RAISE (ABORT, 'Overlapping shifts') END; END"
        );
    }

    @Test
    public void triggerBeforeUpdate() {
        assertEquals(
                ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_UPDATE,
                "CREATE TRIGGER update_trigger BEFORE UPDATE ON shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM shifts WHERE _id != OLD._id AND NOT (end <= NEW.start OR start >= NEW.end)) THEN RAISE (ABORT, 'Overlapping shifts') END; END"
        );
    }

    @Test
    public void deleteEntries() {
        assertEquals(
                ShiftContract.Shift.SQL_DELETE_ENTRIES,
                "DROP TABLE IF EXISTS shifts"
        );
    }

}