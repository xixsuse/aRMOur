package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

import org.threeten.bp.ZoneId;

interface ShiftConfig extends ZoneProvider {
    void process(@NonNull ShiftEntity shift, @NonNull ZoneId zoneId);
}
