package com.skepticalone.mecachecker.behaviours;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.app.DialogFragment;

public interface DetailFragmentBehaviour extends BaseFragmentBehaviour {
    void update(ContentValues contentValues);

    Uri getUpdateContentUri();
    void showDialogFragment(DialogFragment fragment, String tag);
}
