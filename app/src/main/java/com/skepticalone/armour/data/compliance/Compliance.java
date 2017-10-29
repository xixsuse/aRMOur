package com.skepticalone.armour.data.compliance;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;

import java.util.List;

public final class Compliance {

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
    private final RowConsecutiveDays consecutiveDays;
    @Nullable
    private final RowNight night;
    @Nullable
    private final RowLongDay longDay;
    @Nullable
    private final RowRecoveryFollowingNights recoveryFollowingNights;
    @Nullable
    private final RowWeekend weekend;
    @Nullable
    private RowRosteredDayOff rosteredDayOff;

    private boolean compliant;

    public Compliance(
            @NonNull Configuration configuration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        durationOverDay = new RowDurationOverDay(configuration, shift, previousShifts);
        durationOverWeek = new RowDurationOverWeek(configuration, shift, previousShifts);
        durationOverFortnight = new RowDurationOverFortnight(configuration, shift, previousShifts);
        durationBetweenShifts = RowDurationBetweenShifts.from(configuration, shift, previousShifts);
        night = calculateIsNightShift(shift) ? new RowNight(configuration, shift, previousShifts) : null;
        consecutiveDays = night == null ? new RowConsecutiveDays(configuration, shift, previousShifts) : null;
        longDay = night == null && calculateIsLongDay(shift) ? new RowLongDay(configuration, shift, previousShifts) : null;
        recoveryFollowingNights = night == null ? RowRecoveryFollowingNights.from(configuration, shift, previousShifts) : null;
        weekend = RowWeekend.from(configuration, shift, previousShifts);
        compliant =
                durationOverDay.isCompliant() &&
                        durationOverWeek.isCompliant() &&
                        durationOverFortnight.isCompliant() &&
                        (durationBetweenShifts == null || durationBetweenShifts.isCompliant()) &&
                        (consecutiveDays == null || consecutiveDays.isCompliant()) &&
                        (night == null || night.isCompliant()) &&
                        (longDay == null || longDay.isCompliant()) &&
                        (recoveryFollowingNights == null || recoveryFollowingNights.isCompliant());
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
    public final RowRecoveryFollowingNights getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

    @Nullable
    public final RowConsecutiveDays getConsecutiveDays() {
        return consecutiveDays;
    }

    @Nullable
    public final RowNight getNight() {
        return night;
    }

    @Nullable
    public final RowLongDay getLongDay() {
        return longDay;
    }

    @Nullable
    public final RowWeekend getWeekend() {
        return weekend;
    }

    @NonNull
    public final RowDurationOverDay getDurationOverDay() {
        return durationOverDay;
    }

    @NonNull
    public final RowDurationOverWeek getDurationOverWeek() {
        return durationOverWeek;
    }

    @NonNull
    public final RowDurationOverFortnight getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    public final RowDurationBetweenShifts getDurationBetweenShifts() {
        return durationBetweenShifts;
    }

    @Nullable
    public final RowRosteredDayOff getRosteredDayOff() {
        return rosteredDayOff;
    }

    final void setRosteredDayOff(@NonNull RowRosteredDayOff rosteredDayOff) {
        this.rosteredDayOff = rosteredDayOff;
        this.compliant &= rosteredDayOff.isCompliant();
    }

    public final boolean isCompliant() {
        return compliant;
    }

}
