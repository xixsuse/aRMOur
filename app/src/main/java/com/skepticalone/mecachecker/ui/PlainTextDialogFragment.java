package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

abstract class PlainTextDialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends TextDialogFragment<Entity, ViewModel> {

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
