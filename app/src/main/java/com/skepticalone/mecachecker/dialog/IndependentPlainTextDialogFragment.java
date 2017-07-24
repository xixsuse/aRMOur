package com.skepticalone.mecachecker.dialog;

import android.text.InputType;

import com.skepticalone.mecachecker.data.viewModel.BaseViewModel;

abstract class IndependentPlainTextDialogFragment<Entity, ViewModel extends BaseViewModel<Entity>> extends IndependentEditTextDialogFragment<Entity, ViewModel> {

    @Override
    final int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

}
