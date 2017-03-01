package com.skepticalone.mecachecker.data;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlStatements {

    @Test
    public void createEntries() {
        assertEquals(
                "CREATE TABLE shifts (_id INTEGER PRIMARY KEY, start INTEGER NOT NULL, end INTEGER NOT NULL, type INTEGER NOT NULL, CHECK (start < end))",
                ShiftContract.Shift.SQL_CREATE_ENTRIES
        );
    }

    @Test
    public void triggerBeforeInsert() {
        assertEquals(
                "CREATE TRIGGER insert_trigger BEFORE INSERT ON shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM shifts WHERE type == NEW.type AND start < NEW.end AND NEW.start < end) THEN RAISE (ABORT, 'Overlapping shifts') END; END",
                ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_INSERT
        );
    }

    @Test
    public void triggerBeforeUpdate() {
        assertEquals(
                "CREATE TRIGGER update_trigger BEFORE UPDATE ON shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM shifts WHERE type == NEW.type AND _id != OLD._id AND start < NEW.end AND NEW.start < end) THEN RAISE (ABORT, 'Overlapping shifts') END; END",
                ShiftContract.Shift.SQL_CREATE_TRIGGER_BEFORE_UPDATE
        );
    }

    @Test
    public void deleteEntries() {
        assertEquals(
                "DROP TABLE IF EXISTS shifts",
                ShiftContract.Shift.SQL_DELETE_ENTRIES
        );
    }

}