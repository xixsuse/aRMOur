package com.skepticalone.armour.data.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;

import java.util.List;

public final class Compliance {

    @NonNull
    private final ComplianceDataDuration durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    private final ComplianceDataDuration durationBetweenShifts;
    @Nullable
    private final ComplianceDataConsecutiveNights consecutiveNights;
    @Nullable
    private final ComplianceDataConsecutiveDays consecutiveDays;
    @Nullable
    private final ComplianceDataLongDaysPerWeek longDaysPerWeek;
    @Nullable
    private final ComplianceDataConsecutiveWeekends consecutiveWeekends;
    @Nullable
    private final ComplianceDataRecoveryFollowingNights recoveryFollowingNights;
    @Nullable
    private ComplianceDataRosteredDayOff rosteredDayOff;

    private boolean compliant;

    public Compliance(
            @NonNull ComplianceConfiguration complianceConfiguration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        durationOverDay = ComplianceDataDuration.overDay(complianceConfiguration, shift, previousShifts);
        durationOverWeek = ComplianceDataDuration.overWeek(complianceConfiguration, shift, previousShifts);
        durationOverFortnight = ComplianceDataDuration.overFortnight(complianceConfiguration, shift, previousShifts);
        durationBetweenShifts = ComplianceDataDuration.betweenShifts(complianceConfiguration, shift, previousShifts);
        consecutiveNights = ComplianceDataConsecutiveNights.from(complianceConfiguration, shift, previousShifts);
        if (consecutiveNights == null) {
            consecutiveDays = ComplianceDataConsecutiveDays.from(complianceConfiguration, shift, previousShifts);
            longDaysPerWeek = ComplianceDataLongDaysPerWeek.from(complianceConfiguration, shift, previousShifts);
            recoveryFollowingNights = ComplianceDataRecoveryFollowingNights.from(complianceConfiguration, shift, previousShifts);
        } else {
            consecutiveDays = null;
            longDaysPerWeek = null;
            recoveryFollowingNights = null;
        }
        consecutiveWeekends = ComplianceDataConsecutiveWeekends.from(complianceConfiguration, shift, previousShifts);
        compliant =
                durationOverDay.isCompliant() &&
                        durationOverWeek.isCompliant() &&
                        durationOverFortnight.isCompliant() &&
                        (durationBetweenShifts == null || durationBetweenShifts.isCompliant()) &&
                        (consecutiveNights == null || consecutiveNights.isCompliant()) &&
                        (consecutiveDays == null || consecutiveDays.isCompliant()) &&
                        (longDaysPerWeek == null || longDaysPerWeek.isCompliant()) &&
                        (consecutiveWeekends == null || consecutiveWeekends.isCompliant()) &&
                        (recoveryFollowingNights == null || recoveryFollowingNights.isCompliant());
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.ic_compliant_black_24dp : R.drawable.ic_non_compliant_red_24dp;
    }

    @Nullable
    public final ComplianceDataRecoveryFollowingNights getRecoveryFollowingNights() {
        return recoveryFollowingNights;
    }

    @Nullable
    public final ComplianceDataConsecutiveDays getConsecutiveDays() {
        return consecutiveDays;
    }

    @Nullable
    public final ComplianceDataConsecutiveNights getConsecutiveNights() {
        return consecutiveNights;
    }

    @Nullable
    public final ComplianceDataLongDaysPerWeek getLongDaysPerWeek() {
        return longDaysPerWeek;
    }

    @Nullable
    public final ComplianceDataConsecutiveWeekends getConsecutiveWeekends() {
        return consecutiveWeekends;
    }

    @NonNull
    public final ComplianceDataDuration getDurationOverDay() {
        return durationOverDay;
    }

    @NonNull
    public final ComplianceDataDuration getDurationOverWeek() {
        return durationOverWeek;
    }

    @NonNull
    public final ComplianceDataDuration getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    public final ComplianceDataDuration getDurationBetweenShifts() {
        return durationBetweenShifts;
    }

    @Nullable
    public final ComplianceDataRosteredDayOff getRosteredDayOff() {
        return rosteredDayOff;
    }

    final void setRosteredDayOff(@NonNull ComplianceDataRosteredDayOff rosteredDayOff) {
        this.rosteredDayOff = rosteredDayOff;
        this.compliant &= rosteredDayOff.isCompliant();
    }

    public final boolean isCompliant() {
        return compliant;
    }

}
