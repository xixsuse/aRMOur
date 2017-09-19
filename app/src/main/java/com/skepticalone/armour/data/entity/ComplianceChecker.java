package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

import org.threeten.bp.ZoneId;

import java.util.List;

interface ComplianceChecker {
    void process(@NonNull List<RosteredShiftEntity> shifts, @NonNull ZoneId zoneId);
}