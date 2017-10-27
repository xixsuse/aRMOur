package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.compliance.Configuration;

import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RosteredShiftTest {

    @NonNull
    final static Configuration NONE_COMPLIANT = new Configuration(
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
    final List<RosteredShift> getRosteredShifts(@NonNull Configuration complianceConfig) {
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