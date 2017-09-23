//package com.skepticalone.armour.settings;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.preference.DialogPreference;
//import android.util.AttributeSet;
//import android.util.Log;
//
//
//abstract class IntegerPreference extends DialogPreference {
//
//    static final int DEFAULT_VALUE = 0;
//    private static final String TAG = "IntegerPreference";
//    private int mValue;
//    private boolean mValueSet;
//
//    public IntegerPreference(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    final int getValue() {
//        return mValue;
//    }
//
//    final void setValue(int newValue) {
//        final boolean changed = getValue() != newValue;
//        if (changed || !mValueSet) {
//            mValue = newValue;
//            mValueSet = true;
//            persistInt(getValue());
//            if (changed) {
//                notifyChanged();
//            }
//        }
//    }
//
//    @Override
//    protected final Integer onGetDefaultValue(TypedArray a, int index) {
//        int defaultValue = a.getInteger(index, DEFAULT_VALUE);
//        Log.i(TAG, "onGetDefaultValue() returned: " + defaultValue);
//        return defaultValue;
////        return a.getInteger(index, DEFAULT_VALUE);
//    }
//
//    @Override
//    protected final void onSetInitialValue(boolean restorePersistedValue, Object d) {
//        Log.i(TAG, "onSetInitialValue() called with: restorePersistedValue = [" + restorePersistedValue + "], d = [" + d + "]");
//        Log.i(TAG, "onSetInitialValue: key = " + getKey());
//        int defaultValue = (int) d;
//        setValue(restorePersistedValue ? getPersistedInt(defaultValue) : defaultValue);
//    }
//
//}
