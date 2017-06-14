package com.skepticalone.mecachecker.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class MessageFragment extends DialogFragment {
    private static final String MESSAGE = "MESSAGE";

    public static MessageFragment newInstance(@NonNull String message) {
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage(getArguments().getString(MESSAGE)).show();
    }

}
