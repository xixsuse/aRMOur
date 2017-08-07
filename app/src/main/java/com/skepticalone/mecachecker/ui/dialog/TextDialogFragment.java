package com.skepticalone.mecachecker.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.Snackbar;

abstract class TextDialogFragment<Entity> extends DialogFragment<Entity> implements AlertDialog.OnClickListener {

    private EditText editText;
    private Snackbar snackbar;

    @StringRes
    abstract int getTitle();

    abstract void saveText(@Nullable String text);

    @LayoutRes
    abstract int getEditText();

    void onEditTextCreated(@NonNull EditText editText) {
    }

    @SuppressLint("InflateParams")
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        snackbar = (Snackbar) context;
        editText = (EditText) LayoutInflater.from(context).inflate(getEditText(), null, false);
        onEditTextCreated(editText);
    }

    @Override
    public final void onChanged(@Nullable Entity item) {
        if (item != null) {
            String textForDisplay = getTextForDisplay(item);
            if (textForDisplay != null) {
                editText.setText(textForDisplay);
            }
        }
    }

    @Nullable
    abstract String getTextForDisplay(@NonNull Entity item);

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(getTitle())
                .setView(editText)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, this)
                .create();
    }

    @Override
    public final void onClick(DialogInterface dialogInterface, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            String trimmedText = editText.getText().toString().trim();
            saveText(trimmedText.isEmpty() ? null : trimmedText);
        }
    }

    final void showSnackbar(@StringRes int text) {
        snackbar.showSnackbar(text);
    }

}
