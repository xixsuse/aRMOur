package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;

import com.skepticalone.mecachecker.R;

public class PlainTextDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static PlainTextDialogFragment newInstance(@Nullable String text, @StringRes int title, @NonNull Uri contentUri, @NonNull String columnName) {
        PlainTextDialogFragment fragment = new PlainTextDialogFragment();
        Bundle args = getArgs(title, R.layout.input_string, text);
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int getInputType() {
        return InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
    }

    @Override
    void save(@NonNull String trimmedTextWithLength) {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME), trimmedTextWithLength);
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
