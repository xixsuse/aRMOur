package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawRosteredShiftEntity;

import org.threeten.bp.ZoneId;

import java.util.List;

interface ComplianceConfig {
    void process(@NonNull List<RawRosteredShiftEntity> shifts, @NonNull ZoneId zoneId);
}