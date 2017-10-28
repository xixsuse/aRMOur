package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.ShiftSpec;

import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RosteredShiftTest {

    final static MockConfiguration NONE_COMPLIANT = new MockConfiguration();

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