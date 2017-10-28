package com.skepticalone.armour.data.compliance;

import android.support.annotation.RestrictTo;

public abstract class Row {

    private final boolean isChecked;

    Row(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    abstract boolean isCompliantIfChecked();

    public final boolean isCompliant() {
        return !isChecked || isCompliantIfChecked();
    }

}
