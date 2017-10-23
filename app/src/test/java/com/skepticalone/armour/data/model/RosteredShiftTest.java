package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RosteredShiftTest {

    @NonNull
    final static Compliance.Configuration NONE_COMPLIANT = new Compliance.Configuration(
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null
    );

    @NonNull
    final SortedSet<ShiftSpec> shiftSpecs = new TreeSet<>();

    @After
    public final void tearDown() {
        shiftSpecs.clear();
    }

    @NonNull
    final List<RosteredShift> getRosteredShifts(@NonNull Compliance.Configuration complianceConfig) {
        List<RosteredShift> shifts = new ArrayList<>();
        for (ShiftSpec spec : shiftSpecs) {
            shifts.add(spec.toTestShift(
                    complianceConfig,
                    shifts
            ));
        }
        return shifts;
    }

}