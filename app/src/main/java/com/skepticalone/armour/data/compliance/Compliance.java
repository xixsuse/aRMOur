package com.skepticalone.armour.data.compliance;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
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
    private final RowNight night;
    @Nullable
    private final RowLongDay longDay;

    private boolean compliant;

    Compliance(
            @NonNull Configuration configuration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        night = calculateIsNightShift(shift) ? new RowNight(configuration, shift, previousShifts) : null;
        longDay = night == null && calculateIsLongDay(shift) ? new RowLongDay(configuration, shift, previousShifts) : null;
        durationOverDay = new RowDurationOverDay(configuration, shift, previousShifts);
        durationOverWeek = new RowDurationOverWeek(configuration, shift, previousShifts);
        durationOverFortnight = new RowDurationOverFortnight(configuration, shift, previousShifts);
        durationBetweenShifts = RowDurationBetweenShifts.from(configuration, shift, previousShifts);
        compliant =
                durationOverDay.isCompliant() &&
                        durationOverWeek.isCompliant() &&
                        durationOverFortnight.isCompliant() &&
                        (durationBetweenShifts == null || durationBetweenShifts.isCompliant()) &&
                        (night == null || night.isCompliant()) &&
                        (longDay == null || longDay.isCompliant());
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

    private static boolean calculateIsNightShift(Shift.Data shift) {
        return !shift.getStart().toLocalDate().isEqual(shift.getEnd().toLocalDate()) || shift.getStart().toLocalTime().isBefore(EARLIEST_DAY_SHIFT_START);
    }

    private static boolean calculateIsLongDay(Shift.Data shift) {
        return shift.getDuration().compareTo(Duration.ofHours(AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY)) == 1;
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
    }

    @Nullable
    abstract RowConsecutiveDays getConsecutiveDays();

    @Nullable
    abstract RowRecoveryFollowingNights getRecoveryFollowingNights();

    @Nullable
    final RowNight getNight() {
        return night;
    }

    @Nullable
    final RowLongDay getLongDay() {
        return longDay;
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

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    void updateCompliant(boolean compliant) {
        this.compliant &= compliant;
    }

    public final boolean isCompliant() {
        return compliant;
    }

}
