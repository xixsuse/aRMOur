package com.skepticalone.mecachecker.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;


class IntegerPreference extends DialogPreference {

    private EditText mEditText;

    @TargetApi(21)
    public IntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public IntegerPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateDialogView() {
        mEditText = new EditText(getContext());
        mEditText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        return super.onCreateDialogView();
    }
}
