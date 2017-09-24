package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.settings.TimePreference;
import com.skepticalone.armour.util.ShiftType;

import org.threeten.bp.ZoneId;

abstract class ShiftEntity extends ItemEntity implements Shift {

    @NonNull
    @Embedded
    private final ShiftData shiftData;
    @Ignore
    private ShiftType shiftType;

    ShiftEntity(long id,
                @NonNull ShiftData shiftData, @Nullable String comment) {
        super(id, comment);
        this.shiftData = shiftData;
    }

    @NonNull
    @Override
    public final ShiftData getShiftData() {
        return shiftData;
    }

    @NonNull
    @Override
    public ShiftType getShiftType() {
        return shiftType;
    }

    static final class ShiftTypeConfiguration implements ShiftConfig {

        private final int
                normalDayStart,
                normalDayEnd,
                longDayStart,
                longDayEnd,
                nightShiftStart,
                nightShiftEnd;

        @NonNull
        private final ZoneId zoneId;

        ShiftTypeConfiguration(
                int normalDayStart,
                int normalDayEnd,
                int longDayStart,
                int longDayEnd,
                int nightShiftStart,
                int nightShiftEnd,
                @NonNull ZoneId zoneId
        ) {
            this.normalDayStart = normalDayStart;
            this.normalDayEnd = normalDayEnd;
            this.longDayStart = longDayStart;
            this.longDayEnd = longDayEnd;
            this.nightShiftStart = nightShiftStart;
            this.nightShiftEnd = nightShiftEnd;
            this.zoneId = zoneId;
        }

        @NonNull
        @Override
        public ZoneId getZoneId() {
            return zoneId;
        }

        @Override
        public void process(@NonNull ShiftEntity shift, @NonNull ZoneId zoneId) {
            final int start = TimePreference.getTotalMinutes(shift.getShiftData().getStart().atZone(zoneId).toLocalTime()),
                    end = TimePreference.getTotalMinutes(shift.getShiftData().getEnd().atZone(zoneId).toLocalTime());
            if (start == normalDayStart && end == normalDayEnd) {
                shift.shiftType = ShiftType.NORMAL_DAY;
            } else if (start == longDayStart && end == longDayEnd) {
                shift.shiftType = ShiftType.LONG_DAY;
            } else if (start == nightShiftStart && end == nightShiftEnd) {
                shift.shiftType = ShiftType.NIGHT_SHIFT;
            } else {
                shift.shiftType = ShiftType.CUSTOM;
            }
        }
    }
}
