package com.skepticalone.mecachecker.ui.adapter;

interface PayableCallbacks {
    void setClaimed(long id, boolean claimed);

    void setPaid(long id, boolean paid);
}