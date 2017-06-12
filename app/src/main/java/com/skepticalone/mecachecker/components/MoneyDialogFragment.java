package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.widget.Toast;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static MoneyDialogFragment newInstance(@NonNull BigDecimal money, @StringRes int title, @NonNull Uri contentUri, @NonNull String columnName) {
        MoneyDialogFragment fragment = new MoneyDialogFragment();
        Bundle args = getArgs(title, money.toPlainString());
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    private void save(@NonNull BigDecimal money) {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME),
                money.setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue());
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }

    @Override
    void save(@Nullable String trimmedTextWithLength) {
        try {
            save(new BigDecimal(trimmedTextWithLength));
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
        }
    }
}
