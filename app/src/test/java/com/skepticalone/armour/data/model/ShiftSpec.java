package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.compliance.Configuration;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.Collections;
import java.util.List;

import static com.skepticalone.armour.data.model.Item.NO_ID;

public final class ShiftSpec implements Comparable<ShiftSpec> {

    @NonNull
    public static final LocalDate START_DATE = LocalDate.of(2017, 5, 1);
    @NonNull
    private static final ZoneId zoneId = ZoneId.systemDefault();
    @NonNull
    private static final Shift.ShiftType.Configuration DEFAULT_CONFIG = new Shift.ShiftType.Configuration(480, 960, 480, 1350, 1320, 480);
    @NonNull
    private final LocalDateTime start;
    @NonNull
    private final LocalTime end;

    public ShiftSpec(int daysAfterStart, int startHour, int startMinute, int endHour, int endMinute) {
        start = LocalDateTime.of(START_DATE.plusDays(daysAfterStart), LocalTime.of(startHour, startMinute));
        end = LocalTime.of(endHour, endMinute);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShiftSpec) {
            ShiftSpec other = (ShiftSpec) obj;
            return start.isEqual(other.start);
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull ShiftSpec other) {
        return start.compareTo(other.start);
    }

    @NonNull
    public RosteredShift toTestShift(@NonNull Configuration complianceConfig, @Nullable List<RosteredShift> previousShifts) {
        return new RosteredShift(
                new RosteredShiftEntity(NO_ID, null, ShiftData.from(start.atZone(zoneId), end), null),
                zoneId,
                DEFAULT_CONFIG,
                previousShifts == null ? Collections.<RosteredShift>emptyList() : previousShifts,
                complianceConfig
        );
    }

}
