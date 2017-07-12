package com.skepticalone.mecachecker.ui.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;

public class PlainTextDialogFragment extends EditTextDialogFragment {

    private static final String IS_TITLE = "IS_TITLE";
    private Callbacks mCallbacks;

    public static PlainTextDialogFragment newInstance(long id, @Nullable String text, @StringRes int title, boolean isTitle) {
        PlainTextDialogFragment fragment = new PlainTextDialogFragment();
        Bundle args = getArgs(id, title, text, title);
        args.putBoolean(IS_TITLE, isTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
    }

    @Override
    int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

    @Override
    void save(@Nullable String trimmedTextWithLength) {
        mCallbacks.savePlainText(getItemId(), trimmedTextWithLength, getArguments().getBoolean(IS_TITLE, false));
    }

    public interface Callbacks {
        void savePlainText(long itemId, @Nullable String trimmedTextWithLength, boolean isTitle);
    }

}
