package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

public abstract class Row {

    private final boolean isChecked;

    Row(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public abstract boolean isCompliantIfChecked();

    public final boolean isCompliant() {
        return !isChecked || isCompliantIfChecked();
    }

    public final boolean isChecked() {
        return isChecked;
    }

    final boolean equalCompliance(@NonNull Row other) {
        return isChecked ? (other.isChecked && isCompliantIfChecked() == other.isCompliantIfChecked()) : !other.isChecked;
    }
}
