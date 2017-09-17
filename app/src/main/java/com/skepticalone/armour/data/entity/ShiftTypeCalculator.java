package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

interface ShiftTypeCalculator {
    void process(@NonNull ShiftEntity shift);
}
