package com.skepticalone.armour.ui.dialog;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.skepticalone.armour.R;

abstract class PlainTextDialogFragment<Entity> extends TextDialogFragment<Entity> {

    @Override
    final int getEditText() {
        return R.layout.plain_edit_text;
    }

    @Override
    final void onEditTextCreated(@NonNull EditText editText) {
        super.onEditTextCreated(editText);
        editText.setHint(getTitle());
    }

}
