package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

import org.threeten.bp.ZoneId;

public interface ZoneProvider {
    @NonNull
    ZoneId getZoneId();
}
