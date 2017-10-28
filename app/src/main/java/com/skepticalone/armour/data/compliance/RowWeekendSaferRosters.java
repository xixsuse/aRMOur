package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.LocalDate;

import java.util.List;

final class RowWeekendSaferRosters extends RowWeekend {

    private RowWeekendSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(configuration, currentWeekend, previousShifts, 2, configuration.allow1in2Weekends() ? (configuration.allowFrequentConsecutiveWeekends() ? 5 : 6) : (configuration.allowFrequentConsecutiveWeekends() ? 8 : 9));
    }

    @Nullable
    static RowWeekendSaferRosters from(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new RowWeekendSaferRosters(configuration, currentWeekend, previousShifts);
    }

}
