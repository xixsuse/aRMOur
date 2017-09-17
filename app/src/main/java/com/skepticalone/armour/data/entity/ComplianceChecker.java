package com.skepticalone.armour.data.entity;

import android.support.annotation.NonNull;

import java.util.List;

interface ComplianceChecker {
    void process(@NonNull List<RosteredShiftEntity> shifts);
}