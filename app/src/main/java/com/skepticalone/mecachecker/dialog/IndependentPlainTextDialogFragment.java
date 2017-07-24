package com.skepticalone.mecachecker.dialog;

import android.text.InputType;

abstract class IndependentPlainTextDialogFragment<ItemType, ViewModel extends BaseViewModel<ItemType>> extends IndependentEditTextDialogFragment<ItemType, ViewModel> {

    @Override
    final int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

}
