package com.skepticalone.armour.data.db;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContractTest {

    @Test
    public void createTriggers() {
        assertEquals("CREATE TRIGGER trigger_rostered_shifts_insert BEFORE INSERT ON rostered_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT 1 FROM rostered_shifts WHERE (shift_start < NEW.shift_end AND NEW.shift_start < shift_end) OR (logged_shift_start < NEW.logged_shift_end AND NEW.logged_shift_start < logged_shift_end)) THEN RAISE (ABORT, 'Overlap: rostered_shifts_insert') END; END", Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        assertEquals("CREATE TRIGGER trigger_rostered_shifts_update BEFORE UPDATE OF shift_start, shift_end, logged_shift_start, logged_shift_end ON rostered_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT 1 FROM rostered_shifts WHERE _id != NEW._id AND ((shift_start < NEW.shift_end AND NEW.shift_start < shift_end) OR (logged_shift_start < NEW.logged_shift_end AND NEW.logged_shift_start < logged_shift_end))) THEN RAISE (ABORT, 'Overlap: rostered_shifts_update') END; END", Contract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        assertEquals("CREATE TRIGGER trigger_additional_shifts_insert BEFORE INSERT ON additional_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT 1 FROM additional_shifts WHERE shift_start < NEW.shift_end AND NEW.shift_start < shift_end) THEN RAISE (ABORT, 'Overlap: additional_shifts_insert') END; END", Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        assertEquals("CREATE TRIGGER trigger_additional_shifts_update BEFORE UPDATE OF shift_start, shift_end ON additional_shifts BEGIN SELECT CASE WHEN EXISTS (SELECT 1 FROM additional_shifts WHERE _id != NEW._id AND (shift_start < NEW.shift_end AND NEW.shift_start < shift_end)) THEN RAISE (ABORT, 'Overlap: additional_shifts_update') END; END", Contract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
    }

}