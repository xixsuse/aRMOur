package com.skepticalone.mecachecker.data;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlStatements {

    @Test
    public void createTable() {
        assertEquals(
                "CREATE TABLE rostered_shifts (_id INTEGER PRIMARY KEY, scheduled_start INTEGER NOT NULL, scheduled_end INTEGER NOT NULL, logged_start INTEGER DEFAULT NULL, logged_end INTEGER DEFAULT NULL, CHECK (scheduled_start < scheduled_end), CHECK ((logged_start IS NULL AND logged_end IS NULL) OR (logged_start < logged_end)))",
                Contract.RosteredShifts.SQL_CREATE_TABLE
        );
    }

    @Test
    public void createTriggerBeforeInsert() {
        assertEquals(
                "CREATE TRIGGER insert_trigger BEFORE INSERT ON rostered_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM rostered_shifts WHERE ((scheduled_start < NEW.scheduled_end AND NEW.scheduled_start < scheduled_end) OR (logged_start < NEW.logged_end AND NEW.logged_start < logged_end))) THEN RAISE (ABORT, 'Overlapping shifts') END; END",
                Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT
        );
    }

    @Test
    public void createTriggerBeforeUpdate() {
        assertEquals(
                "CREATE TRIGGER update_trigger BEFORE UPDATE ON rostered_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT _id FROM rostered_shifts WHERE _id != OLD._id AND ((scheduled_start < NEW.scheduled_end AND NEW.scheduled_start < scheduled_end) OR (logged_start < NEW.logged_end AND NEW.logged_start < logged_end))) THEN RAISE (ABORT, 'Overlapping shifts') END; END",
                Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE
        );
    }

    @Test
    public void dropTable() {
        assertEquals(
                "DROP TABLE IF EXISTS rostered_shifts",
                Contract.RosteredShifts.SQL_DROP_TABLE
        );
    }

}