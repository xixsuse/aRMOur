package com.skepticalone.armour.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

abstract class IntegerPreference extends DialogPreference {

    static final int DEFAULT_VALUE = 0;
    private int mValue;
    private boolean mValueSet;

    IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    final int getValue() {
        return mValue;
    }

    final void setValue(int newValue) {
        final boolean changed = getValue() != newValue;
        if (changed || !mValueSet) {
            mValue = newValue;
            mValueSet = true;
            persistInt(getValue());
            if (changed) {
                notifyChanged();
            }
        }
    }

    @Override
    protected final Integer onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected final void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(getValue()) : (int) defaultValue);
    }

}
