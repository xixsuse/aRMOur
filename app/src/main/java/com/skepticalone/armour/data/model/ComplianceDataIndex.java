package com.skepticalone.armour.data.model;

public abstract class ComplianceDataIndex extends ComplianceData {

    private final int index;

    ComplianceDataIndex(boolean isChecked, int index) {
        super(isChecked);
        this.index = index;
    }

    public final int getIndex() {
        return index;
    }

}
