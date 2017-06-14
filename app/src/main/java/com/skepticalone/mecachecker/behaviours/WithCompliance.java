package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

public interface WithCompliance {
    int getRowNumberIntervalBetweenShifts();

    int getRowNumberDurationWorkedOverDay();

    int getRowNumberDurationWorkedOverWeek();

    int getRowNumberDurationWorkedOverFortnight();

    int getRowNumberLastWeekendWorked();

    @NonNull
    String getMecaIntervalBetweenShifts();

    @NonNull
    String getMecaDurationOverDay();

    @NonNull
    String getMecaDurationOverWeek();

    @NonNull
    String getMecaDurationOverFortnight();

    @NonNull
    String getMecaPreviousWeekend();
}