package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public interface ExpenseModel extends Model<ExpenseEntity>, SingleAddModel {

    @MainThread
    void setTitle(long id, @NonNull String title);

}
