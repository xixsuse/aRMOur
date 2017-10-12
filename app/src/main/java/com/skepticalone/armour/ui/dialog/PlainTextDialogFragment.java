package com.skepticalone.armour.ui.dialog;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.skepticalone.armour.R;

abstract class PlainTextDialogFragment<FinalItem> extends TextDialogFragment<FinalItem> {

    @Override
    final int getLayout() {
        return R.layout.plain_layout;
    }

    @Override
    final void onEditTextCreated(@NonNull EditText editText) {
        super.onEditTextCreated(editText);
        editText.setHint(getTitle());
    }

}
