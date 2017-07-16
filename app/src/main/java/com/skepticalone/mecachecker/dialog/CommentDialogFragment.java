package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

public final class CommentDialogFragment extends PlainTextDialogFragment {

    private Callbacks callbacks;

    public static CommentDialogFragment newInstance(long id, @Nullable String comment) {
        Bundle args = getArgs(id, R.string.comment, comment, R.string.comment);
        CommentDialogFragment fragment = new CommentDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) getTargetFragment();
    }

    @Override
    final void save(@Nullable String trimmedComment) {
        callbacks.setComment(getItemId(), trimmedComment);
    }

    public interface Callbacks {
        void setComment(long itemId, @Nullable String trimmedComment);
    }

}
