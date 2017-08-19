package com.skepticalone.armour.data.newData;

import android.support.annotation.NonNull;

import java.util.List;

interface ComplianceChecker {
    void process(@NonNull List<RosteredShift> shifts);
}