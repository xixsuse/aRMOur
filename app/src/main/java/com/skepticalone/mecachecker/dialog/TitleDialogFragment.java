package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

public final class TitleDialogFragment extends PlainTextDialogFragment {

    private Callbacks mCallbacks;

    public static TitleDialogFragment newInstance(long id, @NonNull String title) {
        Bundle args = getArgs(id, R.string.title, title, R.string.title);
        TitleDialogFragment fragment = new TitleDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
    }

    @Override
    final void save(@Nullable String trimmedTitle) {
        if (trimmedTitle != null) mCallbacks.saveTitle(getItemId(), trimmedTitle);
    }

    public interface Callbacks {
        void saveTitle(long itemId, @NonNull String trimmedTitle);
    }

}
