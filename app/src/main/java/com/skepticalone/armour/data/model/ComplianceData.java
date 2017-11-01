package com.skepticalone.armour.data.model;

public abstract class ComplianceData {

    private final boolean isChecked;

    ComplianceData(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public abstract boolean isCompliantIfChecked();

    final boolean isCompliant() {
        return !isChecked || isCompliantIfChecked();
    }

    public final boolean isChecked() {
        return isChecked;
    }

}
