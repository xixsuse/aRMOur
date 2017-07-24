package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;

public interface TitleViewModel<Entity> extends BaseViewModel<Entity> {
    void saveNewTitle(@NonNull String newTitle);
}
