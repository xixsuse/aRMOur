package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.view.View;

interface SnackCallbacks {
    void showSnackbar(@NonNull CharSequence text);

    void showSnackbar(@NonNull CharSequence text, @NonNull CharSequence action, @NonNull View.OnClickListener listener);
}
