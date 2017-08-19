package com.skepticalone.armour.data.entity;

import android.arch.core.util.Function;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.ShiftUtil;

import java.util.List;


abstract class ShiftEntity extends ItemEntity implements Shift {

    @NonNull
    @Embedded
    final ShiftData shiftData;
    @Ignore
    ShiftUtil.ShiftType shiftType;

    ShiftEntity(@NonNull ShiftData shiftData, @Nullable String comment) {
        super(comment);
        this.shiftData = shiftData;
    }

    @NonNull
    @Override
    public final ShiftData getShiftData() {
        return shiftData;
    }

    public static final class ShiftTypeChecker<Entity extends ShiftEntity> implements Function<List<Entity>, List<Entity>> {

        private final int
                normalDayStart,
                normalDayEnd,
                longDayStart,
                longDayEnd,
                nightShiftStart,
                nightShiftEnd;

        public ShiftTypeChecker(
                int normalDayStart,
                int normalDayEnd,
                int longDayStart,
                int longDayEnd,
                int nightShiftStart,
                int nightShiftEnd
        ) {
            this.normalDayStart = normalDayStart;
            this.normalDayEnd = normalDayEnd;
            this.longDayStart = longDayStart;
            this.longDayEnd = longDayEnd;
            this.nightShiftStart = nightShiftStart;
            this.nightShiftEnd = nightShiftEnd;
        }

        @Override
        public List<Entity> apply(List<Entity> shifts) {
            for (Entity shift : shifts) {
                int start = shift.shiftData.start.getMinuteOfDay(), end = shift.shiftData.end.getMinuteOfDay();
                shift.shiftType =
                        (start == normalDayStart && end == normalDayEnd) ? ShiftUtil.ShiftType.NORMAL_DAY :
                                (start == longDayStart && end == longDayEnd) ? ShiftUtil.ShiftType.LONG_DAY :
                                        (start == nightShiftStart && end == nightShiftEnd) ? ShiftUtil.ShiftType.NIGHT_SHIFT :
                                                ShiftUtil.ShiftType.CUSTOM;
            }
            return shifts;
        }
    }

}
