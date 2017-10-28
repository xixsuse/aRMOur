package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;

import java.util.List;

public abstract class Compliance {

    private static final LocalTime EARLIEST_DAY_SHIFT_START = LocalTime.of(6, 0);
    @NonNull
    private final RowDurationOverDay durationOverDay;
    @NonNull
    private final RowDurationOverWeek durationOverWeek;
    @NonNull
    private final RowDurationOverFortnight durationOverFortnight;
    @Nullable
    private final RowDurationBetweenShifts durationBetweenShifts;
    @Nullable
    private final RowLongDaysOverWeek longDaysOverWeek;
    @NonNull
    private final ShiftType shiftType;

    Compliance(
            @NonNull Configuration configuration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        shiftType = calculateShiftType(shift);
        longDaysOverWeek = shiftType == ShiftType.LONG_DAY ? new RowLongDaysOverWeek(configuration, shift, previousShifts) : null;
        durationOverDay = new RowDurationOverDay(configuration, shift, previousShifts);
        durationOverWeek = new RowDurationOverWeek(configuration, shift, previousShifts);
        durationOverFortnight = new RowDurationOverFortnight(configuration, shift, previousShifts);
        durationBetweenShifts = RowDurationBetweenShifts.from(configuration, shift, previousShifts);
    }

    @NonNull
    public static Compliance from(
            @NonNull Configuration configuration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        if (configuration instanceof ConfigurationSaferRosters) {
            return new ComplianceSaferRosters((ConfigurationSaferRosters) configuration, shift, previousShifts);
        } else {
            return new ComplianceLegacy(configuration, shift, previousShifts);
        }
    }

    @NonNull
    private static ShiftType calculateShiftType(@NonNull com.skepticalone.armour.data.model.Shift.Data shift) {
        if (shift.getStart().toLocalDate().isEqual(shift.getEnd().toLocalDate()) || shift.getStart().toLocalTime().isBefore(EARLIEST_DAY_SHIFT_START)) {
            return ShiftType.NIGHT_SHIFT;
        } else if (shift.getDuration().compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY)) == 1) {
            return ShiftType.LONG_DAY;
        } else {
            return ShiftType.NORMAL_DAY;
        }
    }

    @Nullable
    abstract RowConsecutiveDays getConsecutiveDays();

    @Nullable
    abstract RowConsecutiveNights getConsecutiveNights();

    @Nullable
    abstract RowRecoveryFollowingNights getRecoveryFollowingNights();

    @Nullable
    final RowLongDaysOverWeek getLongDaysOverWeek() {
        return longDaysOverWeek;
    }

    @NonNull
    final ShiftType getShiftType() {
        return shiftType;
    }

    @Nullable
    abstract RowWeekend getWeekend();

    @NonNull
    final RowDurationOverDay getDurationOverDay() {
        return durationOverDay;
    }

    @NonNull
    final RowDurationOverWeek getDurationOverWeek() {
        return durationOverWeek;
    }

    @NonNull
    final RowDurationOverFortnight getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    final RowDurationBetweenShifts getDurationBetweenShifts() {
        return durationBetweenShifts;
    }

    enum ShiftType {
        NORMAL_DAY,
        LONG_DAY,
        NIGHT_SHIFT
    }

}
