package com.skepticalone.armour.data.compliance;

public abstract class Row {

    private final boolean isChecked;

    Row(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public abstract boolean isCompliantIfChecked();

    public final boolean isChecked() {
        return isChecked;
    }

}
