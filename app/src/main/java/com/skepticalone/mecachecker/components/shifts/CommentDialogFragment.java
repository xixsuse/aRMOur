package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

public class CommentDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private Uri mContentUri;
    private String mColumnName;

    public static CommentDialogFragment newInstance(@Nullable String comment, Uri contentUri, String columnName) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = getArgs(R.string.comment, R.layout.comment_input, comment);
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContentUri = getArguments().getParcelable(CONTENT_URI);
        mColumnName = getArguments().getString(COLUMN_NAME);
    }

    @Override
    void save() {
        ContentValues values = new ContentValues();
        values.put(mColumnName, getText());
        getActivity().getContentResolver().update(mContentUri, values, null, null);
    }
}
