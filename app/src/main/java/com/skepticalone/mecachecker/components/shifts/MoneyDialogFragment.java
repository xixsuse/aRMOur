package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.InputType;

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
    int getInputType() {
        return InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    @Override
    void save(@NonNull String trimmedTextWithLength) {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME),
                new BigDecimal(trimmedTextWithLength).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue());
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
