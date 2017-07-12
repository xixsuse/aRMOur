package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.Nullable;

public interface PayableCallbacks {
    void changeComment(long itemId, @Nullable String currentComment);

    void setClaimed(long itemId, boolean claimed);

    void setPaid(long itemId, boolean paid);
}