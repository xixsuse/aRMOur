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
    private final ComplianceDataNight night;
    @Nullable
    private final ComplianceDataConsecutiveDays consecutiveDays;
    @Nullable
    private final ComplianceDataLongDay longDay;
    @Nullable
    private final ComplianceDataWeekend weekend;
    @Nullable
    private final ComplianceDataRecoveryFollowingNights recoveryFollowingNights;
    @Nullable
    private ComplianceDataRosteredDayOff rosteredDayOff;

    private boolean compliant;

    public Compliance(
            @NonNull Configuration configuration,
            @NonNull com.skepticalone.armour.data.model.Shift.Data shift,
            @NonNull List<RosteredShift> previousShifts
    ) {
        durationOverDay = ComplianceDataDuration.overDay(configuration, shift, previousShifts);
        durationOverWeek = ComplianceDataDuration.overWeek(configuration, shift, previousShifts);
        durationOverFortnight = ComplianceDataDuration.overFortnight(configuration, shift, previousShifts);
        durationBetweenShifts = ComplianceDataDuration.betweenShifts(configuration, shift, previousShifts);
        night = ComplianceDataNight.from(configuration, shift, previousShifts);
        if (night == null) {
            consecutiveDays = ComplianceDataConsecutiveDays.from(configuration, shift, previousShifts);
            longDay = ComplianceDataLongDay.from(configuration, shift, previousShifts);
            recoveryFollowingNights = ComplianceDataRecoveryFollowingNights.from(configuration, shift, previousShifts);
        } else {
            consecutiveDays = null;
            longDay = null;
            recoveryFollowingNights = null;
        }
        weekend = ComplianceDataWeekend.from(configuration, shift, previousShifts);
        compliant =
                durationOverDay.isCompliant() &&
                        durationOverWeek.isCompliant() &&
                        durationOverFortnight.isCompliant() &&
                        (durationBetweenShifts == null || durationBetweenShifts.isCompliant()) &&
                        (night == null || night.isCompliant()) &&
                        (consecutiveDays == null || consecutiveDays.isCompliant()) &&
                        (longDay == null || longDay.isCompliant()) &&
                        (weekend == null || weekend.isCompliant()) &&
                        (recoveryFollowingNights == null || recoveryFollowingNights.isCompliant());
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
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
    public final ComplianceDataNight getNight() {
        return night;
    }

    @Nullable
    public final ComplianceDataLongDay getLongDay() {
        return longDay;
    }

    @Nullable
    public final ComplianceDataWeekend getWeekend() {
        return weekend;
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
