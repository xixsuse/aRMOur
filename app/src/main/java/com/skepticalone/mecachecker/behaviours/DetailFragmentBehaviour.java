package com.skepticalone.mecachecker.behaviours;

import android.content.ContentValues;
import android.support.v4.app.DialogFragment;

public interface DetailFragmentBehaviour extends BaseFragmentBehaviour {
    void update(ContentValues contentValues);

    void showDialogFragment(DialogFragment fragment, String tag);
}
