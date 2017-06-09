package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static MoneyDialogFragment newInstance(@NonNull BigDecimal money, @StringRes int title, @NonNull Uri contentUri, @NonNull String columnName) {
        MoneyDialogFragment fragment = new MoneyDialogFragment();
        Bundle args = getArgs(title, R.layout.input_money, money.toPlainString());
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void save() {
        ContentValues values = new ContentValues();
        String text = getText();
        if (text == null) return;
        values.put(getArguments().getString(COLUMN_NAME),
                new BigDecimal(text).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue());
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
