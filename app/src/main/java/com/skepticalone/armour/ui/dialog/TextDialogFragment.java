package com.skepticalone.armour.ui.dialog;

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
import android.view.View;
import android.widget.EditText;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.Snackbar;

abstract class TextDialogFragment<Entity> extends DialogFragment<Entity> implements AlertDialog.OnClickListener {

    private View layout;
    private EditText editText;
    private Snackbar snackbar;

    @StringRes
    abstract int getTitle();

    abstract void saveText(@Nullable String text);

    @LayoutRes
    abstract int getLayout();

    void onEditTextCreated(@NonNull EditText editText) {
    }

    @SuppressLint("InflateParams")
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        snackbar = (Snackbar) context;
        layout = LayoutInflater.from(context).inflate(getLayout(), null, false);
        editText = layout.findViewById(R.id.edit_text);
        onEditTextCreated(editText);
    }

    @Override
    final void onUpdateView(@NonNull Entity item) {
        String textForDisplay = getTextForDisplay(item);
        if (textForDisplay != null) {
            editText.setText(textForDisplay);
        }
    }

    @Nullable
    abstract String getTextForDisplay(@NonNull Entity item);

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(getTitle())
                .setView(layout)
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
