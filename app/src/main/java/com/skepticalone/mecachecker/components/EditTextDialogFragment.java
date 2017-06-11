package com.skepticalone.mecachecker.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;


abstract public class EditTextDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = "EditTextDialogFragment";
    private static final String TEXT = "TEXT";
    private static final String TITLE_ID = "TITLE_ID";
    private static final String LAYOUT_ID = "LAYOUT_ID";
    private EditText editText;

    static Bundle getArgs(@StringRes int title, @LayoutRes int layout, @Nullable String text) {
        Bundle args = new Bundle();
        args.putInt(TITLE_ID, title);
        args.putInt(LAYOUT_ID, layout);
        args.putString(TEXT, text);
        return args;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editText = (EditText) LayoutInflater.from(context).inflate(getArguments().getInt(LAYOUT_ID), null, false);
        editText.setInputType(getInputType());
        editText.setText(getArguments().getString(TEXT));
    }

    abstract int getInputType();

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getInt(TITLE_ID))
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, this)
                .setView(editText);
        return builder.create();
    }

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            String trimmedText = editText.getText().toString().trim();
            save(trimmedText.length() == 0 ? null : trimmedText);
        }
    }

    abstract void save(@Nullable String trimmedTextWithLength);

}
