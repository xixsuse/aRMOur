package com.skepticalone.mecachecker.dialog;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;

public class PlainTextDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static PlainTextDialogFragment newInstance(@Nullable String text, @StringRes int title, @NonNull Uri contentUri, @NonNull String columnName) {
        PlainTextDialogFragment fragment = new PlainTextDialogFragment();
        Bundle args = getArgs(title, text, title);
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

    @Override
    void save(@Nullable String trimmedTextWithLength) {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME), trimmedTextWithLength);
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
