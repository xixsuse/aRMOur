package com.skepticalone.mecachecker.dialog;

import android.text.InputType;

abstract class PlainTextDialogFragment extends EditTextDialogFragment {

    @Override
    final int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

}
