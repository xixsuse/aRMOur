package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

abstract class RowWeekend extends Row {

    @NonNull
    private final LocalDate currentWeekend;
    private final int numerator, calculatedNumerator;

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    RowWeekend(@NonNull Configuration configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts, int numerator, int denominator) {
        super(configuration.checkFrequencyOfWeekends());
        this.currentWeekend = currentWeekend;
        this.numerator = numerator;
        LocalDate cutOff = currentWeekend.minusWeeks(denominator);
        int indexOfWeekendInPeriod = 0;
        LocalDate lastWeekendCounted = currentWeekend;
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            RowWeekend previousWeekend = previousShifts.get(i).getCompliance().getWeekend();
            if (previousWeekend != null) {
                if (!previousWeekend.currentWeekend.isAfter(cutOff)) {
                    break;
                } else if (!previousWeekend.currentWeekend.isEqual(lastWeekendCounted)) {
                    indexOfWeekendInPeriod++;
                    lastWeekendCounted = previousWeekend.currentWeekend;
                }
            }
        }
        calculatedNumerator = indexOfWeekendInPeriod + 1;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Nullable
    static LocalDate calculateCurrentWeekend(@NonNull Shift.Data shift) {
        ZonedDateTime weekendStart = shift.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        return weekendStart.isBefore(shift.getEnd()) && shift.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
    }

    @NonNull
    final LocalDate getCurrentWeekend() {
        return currentWeekend;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return calculatedNumerator <= numerator;
    }
}
