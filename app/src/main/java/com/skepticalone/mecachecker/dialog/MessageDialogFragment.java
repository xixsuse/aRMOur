package com.skepticalone.mecachecker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class MessageDialogFragment extends DialogFragment {
    private static final String MESSAGE = "MESSAGE";

    public static MessageDialogFragment newInstance(@NonNull String message) {
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage(getArguments().getString(MESSAGE)).show();
    }

}
