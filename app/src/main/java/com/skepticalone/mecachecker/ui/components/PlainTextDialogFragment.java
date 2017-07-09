package com.skepticalone.mecachecker.ui.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.util.Log;

public class PlainTextDialogFragment extends EditTextDialogFragment {

    //    private static final String CONTENT_URI = "CONTENT_URI";
//    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String TAG = "PlainTextDialogFragment";
    private Callbacks mCallbacks;

    public static PlainTextDialogFragment newInstance(long id, @Nullable String text, @StringRes int title) {
        // } @NonNull Uri contentUri, @NonNull String columnName) {
        PlainTextDialogFragment fragment = new PlainTextDialogFragment();
        Bundle args = getArgs(id, title, text, title);
//        args.putParcelable(CONTENT_URI, contentUri);
//        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
    }

    @Override
    int getInputType() {
        return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    }

    @Override
    void save(@Nullable String trimmedTextWithLength) {
        Log.i(TAG, "save: " + trimmedTextWithLength);
        mCallbacks.savePlainText(getItemId(), trimmedTextWithLength);
//        ContentValues values = new ContentValues();
//        values.put(getArguments().getString(COLUMN_NAME), trimmedTextWithLength);
//        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
//        assert contentUri != null;
//        getActivity().getContentResolver().update(contentUri, values, null, null);
    }

    public interface Callbacks {
        void savePlainText(long itemId, @Nullable String trimmedTextWithLength);
    }

}
