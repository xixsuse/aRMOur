package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

interface SnackCallbacks {
    void showSnackbar(@StringRes int text);
    void showSnackbar(@StringRes int text, @StringRes int action, @NonNull View.OnClickListener listener);
}
