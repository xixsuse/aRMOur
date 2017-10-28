package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class RowWeekendLegacy extends RowWeekend {

    @Nullable
    private final LocalDate previousWeekend;

    private RowWeekendLegacy(@NonNull Configuration configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, currentWeekend, previousShifts, 1, configuration.allow1in2Weekends() ? AppConstants.MAXIMUM_WEEKEND_FREQUENCY_DENOMINATOR_LENIENT : AppConstants.MAXIMUM_WEEKEND_FREQUENCY_DENOMINATOR_STRICT);
        previousWeekend = calculatePreviousWeekend(getCurrentWeekend(), previousShifts);
    }

    @Nullable
    static RowWeekendLegacy from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new RowWeekendLegacy(configuration, currentWeekend, previousShifts);
    }

    @Nullable
    private static LocalDate calculatePreviousWeekend(@NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            RowWeekendLegacy previousShiftWeekend = (RowWeekendLegacy) previousShifts.get(i).getCompliance().getWeekend();
            if (previousShiftWeekend != null) {
                return previousShiftWeekend.getCurrentWeekend().isEqual(currentWeekend) ? previousShiftWeekend.previousWeekend : previousShiftWeekend.getCurrentWeekend();
            }
        }
        return null;
    }

    @Nullable
    public LocalDate getPreviousWeekend() {
        return previousWeekend;
    }
    //
//    @Override
//    public boolean isCompliantIfChecked() {
//        return previousWeekend == null || !getCurrentWeekend().minusWeeks(1).isEqual(previousWeekend);
//    }
}
