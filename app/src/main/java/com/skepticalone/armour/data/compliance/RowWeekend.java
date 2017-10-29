package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class RowWeekend extends Row {

    @NonNull
    private final LocalDate currentWeekend;
    private final int numerator, denominator, calculatedNumerator;

    private RowWeekend(@NonNull Configuration configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkFrequencyOfWeekends());
        this.currentWeekend = currentWeekend;
        if (configuration instanceof ConfigurationSaferRosters) {
            ConfigurationSaferRosters configurationSaferRosters = (ConfigurationSaferRosters) configuration;
            this.numerator = 2;
            this.denominator = configurationSaferRosters.allow1in2Weekends() ? (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 5 : 6) : (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 8 : 9);
        } else {
            this.numerator = 1;
            this.denominator = configuration.allow1in2Weekends() ? AppConstants.MAXIMUM_WEEKEND_FREQUENCY_DENOMINATOR_LENIENT : AppConstants.MAXIMUM_WEEKEND_FREQUENCY_DENOMINATOR_STRICT;
        }
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

    @Nullable
    static RowWeekend from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new RowWeekend(configuration, currentWeekend, previousShifts);
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Nullable
    private static LocalDate calculateCurrentWeekend(@NonNull Shift.Data shift) {
        ZonedDateTime weekendStart = shift.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        return weekendStart.isBefore(shift.getEnd()) && shift.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
    }

//    @Nullable
//    private static LocalDate calculatePreviousWeekend(@NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
//        for (int i = previousShifts.size() - 1; i >= 0; i--) {
//            RowWeekend previousShiftWeekend = previousShifts.get(i).getCompliance().getWeekend();
//            if (previousShiftWeekend != null) {
//                return previousShiftWeekend.getCurrentWeekend().isEqual(currentWeekend) ? previousShiftWeekend.previousWeekend : previousShiftWeekend.getCurrentWeekend();
//            }
//        }
//        return null;
//    }

    @NonNull
    public final LocalDate getCurrentWeekend() {
        return currentWeekend;
    }

    public final int getNumerator() {
        return numerator;
    }

    final int getCalculatedNumerator() {
        return calculatedNumerator;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return calculatedNumerator <= numerator;
    }

    public static final class Binder extends Row.Binder<RowWeekend> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowWeekend row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_weekend_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return "Weekend info";
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            return getRow().calculatedNumerator + " out of " + getRow().denominator;
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(
                    R.string.meca_safer_rosters_maximum_consecutive_weekends,
                    getRow().numerator,
                    getRow().denominator,
                    getRow().denominator - getRow().numerator
            );
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            if (!super.areContentsTheSame(other)) return false;
            Binder newBinder = (Binder) other;
            return
                    getRow().currentWeekend.isEqual(newBinder.getRow().currentWeekend) &&
                            getRow().numerator == newBinder.getRow().numerator &&
                            getRow().denominator == newBinder.getRow().denominator &&
                            getRow().calculatedNumerator == newBinder.getRow().calculatedNumerator;
        }
    }

}
