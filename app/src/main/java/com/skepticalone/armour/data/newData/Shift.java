package com.skepticalone.armour.data.newData;

import android.support.annotation.NonNull;

import com.skepticalone.armour.util.ShiftUtil;

abstract class Shift extends RawShift {

    @NonNull
    private final ShiftUtil.ShiftType shiftType;

    Shift(long start, long end, @NonNull ShiftTypeCalculator shiftTypeCalculator) {
        super(start, end);
        shiftType = shiftTypeCalculator.getShiftType(interval);
    }

    @Override
    public String toString() {
        return shiftType.name() + ": " + super.toString();
    }

//
//    public final ShiftUtil.ShiftType getShiftType() {
//        return shiftType;
//    }

    //    static final class ShiftTypeCalculator {
//
//        private final int
//                normalDayStart,
//                normalDayEnd,
//                longDayStart,
//                longDayEnd,
//                nightShiftStart,
//                nightShiftEnd;
//
//        public ShiftTypeChecker(
//                int normalDayStart,
//                int normalDayEnd,
//                int longDayStart,
//                int longDayEnd,
//                int nightShiftStart,
//                int nightShiftEnd
//        ) {
//            this.normalDayStart = normalDayStart;
//            this.normalDayEnd = normalDayEnd;
//            this.longDayStart = longDayStart;
//            this.longDayEnd = longDayEnd;
//            this.nightShiftStart = nightShiftStart;
//            this.nightShiftEnd = nightShiftEnd;
//        }
//
//        ShiftUtil.ShiftType getShiftType(Shift shift) {
//
//        }
//    }
}
