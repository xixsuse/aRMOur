package com.skepticalone.mecachecker.ui;

public interface ExpenseUpdateCallback {
    void setClaimed(long id, boolean claimed);

    void setPaid(long id, boolean paid);
}
